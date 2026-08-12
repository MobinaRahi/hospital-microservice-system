package hospital.billingservice.service.impl;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemUpdateDto;
import hospital.billingservice.exception.invoiceitem.CannotAddItemToInvoiceException;
import hospital.billingservice.exception.invoiceitem.InvoiceItemNotFoundException;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.mapper.InvoiceItemMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.InvoiceItem;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.repository.InvoiceItemRepository;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.service.InvoiceItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link InvoiceItemService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemMapper invoiceItemMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InvoiceItemResponseDto addItemToInvoice(Long invoiceId, InvoiceItemCreateDto dto) {
        log.info("Adding item to invoice: {}", invoiceId);

        // Validate invoice exists and is PENDING
        Invoice invoice = invoiceRepository.findNotDeletedById(invoiceId)
                .orElseThrow(() -> InvoiceNotFoundException.byId(invoiceId));

        if (invoice.getStatus() != InvoiceStatus.PENDING) {
            throw new CannotAddItemToInvoiceException(invoiceId, invoice.getStatus().name());
        }

        // Map DTO to entity
        InvoiceItem item = invoiceItemMapper.toEntity(dto);
        item.setInvoice(invoice);
        item.recalculateTotalPrice();

        // Save and return
        InvoiceItem saved = invoiceItemRepository.save(item);
        log.info("Invoice item created with id: {}", saved.getId());

        // Recalculate invoice totals
        invoice.recalculateTotals();
        invoiceRepository.save(invoice);

        return invoiceItemMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public InvoiceItemResponseDto getItemById(Long id) {
        log.debug("Fetching invoice item by id: {}", id);

        InvoiceItem item = invoiceItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceItemNotFoundException.byId(id));

        return invoiceItemMapper.toResponseDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceItemResponseDto> getItemsByInvoice(Long invoiceId) {
        log.debug("Fetching items for invoice: {}", invoiceId);

        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceId(invoiceId);
        return invoiceItemMapper.toResponseDtoList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceItemResponseDto> getItemsByServiceCode(String serviceCode) {
        log.debug("Fetching items by service code: {}", serviceCode);

        List<InvoiceItem> items = invoiceItemRepository.findByServiceCode(serviceCode);
        return invoiceItemMapper.toResponseDtoList(items);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InvoiceItemResponseDto updateItem(Long id, InvoiceItemUpdateDto dto) {
        log.info("Updating invoice item id: {}", id);

        InvoiceItem item = invoiceItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceItemNotFoundException.byId(id));

        // Map update DTO to entity
        invoiceItemMapper.updateEntity(dto, item);

        // Recalculate total price
        item.recalculateTotalPrice();
        InvoiceItem saved = invoiceItemRepository.save(item);

        // Recalculate invoice totals
        Invoice invoice = item.getInvoice();
        invoice.recalculateTotals();
        invoiceRepository.save(invoice);

        log.info("Invoice item updated id: {}", saved.getId());

        return invoiceItemMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    @Override
    public void deleteItem(Long id) {
        log.info("Soft-deleting invoice item id: {}", id);

        InvoiceItem item = invoiceItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> InvoiceItemNotFoundException.byId(id));

        Invoice invoice = item.getInvoice();

        item.softDelete(null);
        invoiceItemRepository.save(item);

        // Recalculate invoice totals
        invoice.recalculateTotals();
        invoiceRepository.save(invoice);
    }
}
