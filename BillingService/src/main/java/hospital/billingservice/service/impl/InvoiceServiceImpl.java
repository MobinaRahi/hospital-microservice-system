package hospital.billingservice.service.impl;

import hospital.billingservice.dto.invoice.InvoiceCreateDto;
import hospital.billingservice.dto.invoice.InvoiceResponseDto;
import hospital.billingservice.dto.invoice.InvoiceUpdateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.exception.invoice.DuplicateInvoiceNumberException;
import hospital.billingservice.exception.invoice.IllegalInvoiceStatusException;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.mapper.InvoiceMapper;
import hospital.billingservice.mapper.InvoiceItemMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.InvoiceItem;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.repository.InvoiceItemRepository;
import hospital.billingservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link InvoiceService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InvoiceResponseDto createInvoice(InvoiceCreateDto dto) {
        log.info("Creating invoice: {}", dto.getInvoiceNumber());

        // Validate unique invoice number
        if (invoiceRepository.existsByInvoiceNumber(dto.getInvoiceNumber())) {
            throw new DuplicateInvoiceNumberException(dto.getInvoiceNumber());
        }

        // Map DTO to entity
        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setCreatedByUser(null); // Will be set from SecurityContext later

        // Save invoice first
        Invoice saved = invoiceRepository.save(invoice);

        // Create invoice items
        for (InvoiceItemCreateDto itemDto : dto.getItems()) {
            InvoiceItem item = invoiceItemMapper.toEntity(itemDto);
            item.setInvoice(saved);
            item.recalculateTotalPrice();
            invoiceItemRepository.save(item);
        }

        // Recalculate totals
        saved.recalculateTotals();
        saved = invoiceRepository.save(saved);

        log.info("Invoice created with id: {}", saved.getId());

        return invoiceMapper.toResponseDto(saved);
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(Long id) {
        log.debug("Fetching invoice by id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        return invoiceMapper.toResponseDto(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceByNumber(String invoiceNumber) {
        log.debug("Fetching invoice by number: {}", invoiceNumber);

        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> InvoiceNotFoundException.byNumber(invoiceNumber));

        return invoiceMapper.toResponseDto(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByPatient(Long patientId) {
        log.debug("Fetching invoices for patient: {}", patientId);

        List<Invoice> invoices = invoiceRepository.findByPatientId(patientId);
        return invoiceMapper.toResponseDtoList(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status) {
        log.debug("Fetching invoices by status: {}", status);

        List<Invoice> invoices = invoiceRepository.findByStatus(status);
        return invoiceMapper.toResponseDtoList(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getOverdueInvoices() {
        log.debug("Fetching overdue invoices");

        List<Invoice> invoices = invoiceRepository.findOverdueInvoices(LocalDate.now());
        return invoiceMapper.toResponseDtoList(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getOverdueInvoicesAsOf(LocalDate date) {
        log.debug("Fetching overdue invoices as of: {}", date);

        List<Invoice> invoices = invoiceRepository.findOverdueInvoices(date);
        return invoiceMapper.toResponseDtoList(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching invoices by date range: {} to {}", startDate, endDate);

        List<Invoice> invoices = invoiceRepository.findByIssueDateBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return invoiceMapper.toResponseDtoList(invoices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByEncounter(Long encounterId) {
        log.debug("Fetching invoices for encounter: {}", encounterId);

        List<Invoice> invoices = invoiceRepository.findByEncounterId(encounterId);
        return invoiceMapper.toResponseDtoList(invoices);
    }

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InvoiceResponseDto updateInvoice(Long id, InvoiceUpdateDto dto) {
        log.info("Updating invoice id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        // Map update DTO to entity
        invoiceMapper.updateEntity(dto, invoice);

        // Recalculate totals if discount/tax/insurance changed
        invoice.recalculateTotals();
        Invoice saved = invoiceRepository.save(invoice);

        log.info("Invoice updated id: {}", saved.getId());

        return invoiceMapper.toResponseDto(saved);
    }

    // ══════════════════════════════════════════════════════════════════
    // Status Transitions
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InvoiceResponseDto cancelInvoice(Long id) {
        log.info("Cancelling invoice id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        if (!invoice.canBeCancelled()) {
            throw IllegalInvoiceStatusException.cannotCancel(invoice.getStatus().name());
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        Invoice saved = invoiceRepository.save(invoice);

        return invoiceMapper.toResponseDto(saved);
    }

    @Override
    public InvoiceResponseDto markAsPaid(Long id) {
        log.info("Marking invoice as paid id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        if (invoice.getStatus() != InvoiceStatus.PARTIAL && invoice.getStatus() != InvoiceStatus.PENDING) {
            throw IllegalInvoiceStatusException.cannotMarkPaid(invoice.getStatus().name());
        }

        invoice.setStatus(InvoiceStatus.PAID);
        Invoice saved = invoiceRepository.save(invoice);

        return invoiceMapper.toResponseDto(saved);
    }

    @Override
    public InvoiceResponseDto updateInvoiceStatusFromPayments(Long id) {
        log.info("Updating invoice status from payments id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        // This would typically calculate total payments and update status
        // For now, just return the current state
        return invoiceMapper.toResponseDto(invoice);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deleteInvoice(Long id) {
        log.info("Soft-deleting invoice id: {}", id);

        Invoice invoice = invoiceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceNotFoundException.byId(id));

        invoice.softDelete(null);
        invoiceRepository.save(invoice);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean invoiceNumberExists(String invoiceNumber) {
        return invoiceRepository.existsByInvoiceNumber(invoiceNumber);
    }
}
