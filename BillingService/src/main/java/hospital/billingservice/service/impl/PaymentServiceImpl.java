package hospital.billingservice.service.impl;

import hospital.billingservice.dto.payment.PaymentCreateDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.dto.payment.PaymentUpdateDto;
import hospital.billingservice.exception.invoice.InvoiceNotFoundException;
import hospital.billingservice.exception.payment.DuplicatePaymentReferenceException;
import hospital.billingservice.exception.payment.PaymentExceedsBalanceException;
import hospital.billingservice.exception.payment.PaymentNotFoundException;
import hospital.billingservice.mapper.PaymentMapper;
import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.Payment;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.model.enums.PaymentMethod;
import hospital.billingservice.repository.InvoiceRepository;
import hospital.billingservice.repository.PaymentRepository;
import hospital.billingservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link PaymentService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PaymentResponseDto createPayment(PaymentCreateDto dto) {
        log.info("Creating payment for invoice: {}", dto.getInvoiceId());

        // Validate invoice exists
        Invoice invoice = invoiceRepository.findNotDeletedById(dto.getInvoiceId())
                .orElseThrow(() -> InvoiceNotFoundException.byId(dto.getInvoiceId()));

        // Validate payment doesn't exceed balance
        BigDecimal remainingBalance = getRemainingBalance(dto.getInvoiceId());
        if (dto.getAmount().compareTo(remainingBalance) > 0) {
            throw new PaymentExceedsBalanceException(dto.getInvoiceId(), dto.getAmount(), remainingBalance);
        }

        // Validate unique reference number
        if (dto.getReferenceNumber() != null && paymentRepository.existsByReferenceNumber(dto.getReferenceNumber())) {
            throw DuplicatePaymentReferenceException.byReferenceNumber(dto.getReferenceNumber());
        }

        // Validate unique receipt number
        if (dto.getReceiptNumber() != null && paymentRepository.existsByReceiptNumber(dto.getReceiptNumber())) {
            throw DuplicatePaymentReferenceException.byReceiptNumber(dto.getReceiptNumber());
        }

        // Map DTO to entity
        Payment payment = paymentMapper.toEntity(dto);
        payment.setInvoice(invoice);

        // Save and return
        Payment saved = paymentRepository.save(payment);
        log.info("Payment created with id: {}", saved.getId());

        // Update invoice status
        updateInvoiceStatus(invoice);

        return paymentMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        log.debug("Fetching payment by id: {}", id);

        Payment payment = paymentRepository.findNotDeletedById(id)
                .orElseThrow(() -> PaymentNotFoundException.byId(id));

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByInvoice(Long invoiceId) {
        log.debug("Fetching payments for invoice: {}", invoiceId);

        List<Payment> payments = paymentRepository.findByInvoiceId(invoiceId);
        return paymentMapper.toResponseDtoList(payments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByMethod(PaymentMethod method) {
        log.debug("Fetching payments by method: {}", method);

        List<Payment> payments = paymentRepository.findByMethod(method);
        return paymentMapper.toResponseDtoList(payments);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByReferenceNumber(String referenceNumber) {
        log.debug("Fetching payment by reference number: {}", referenceNumber);

        Payment payment = paymentRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> PaymentNotFoundException.byReferenceNumber(referenceNumber));

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByReceiptNumber(String receiptNumber) {
        log.debug("Fetching payment by receipt number: {}", receiptNumber);

        Payment payment = paymentRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> PaymentNotFoundException.byId(null));

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching payments by date range: {} to {}", startDate, endDate);

        List<Payment> payments = paymentRepository.findByPaymentDateBetween(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return paymentMapper.toResponseDtoList(payments);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaidForInvoice(Long invoiceId) {
        log.debug("Calculating total paid for invoice: {}", invoiceId);

        List<Payment> payments = paymentRepository.findByInvoiceId(invoiceId);
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getRemainingBalance(Long invoiceId) {
        log.debug("Calculating remaining balance for invoice: {}", invoiceId);

        Invoice invoice = invoiceRepository.findNotDeletedById(invoiceId)
                .orElseThrow(() -> InvoiceNotFoundException.byId(invoiceId));

        BigDecimal totalPaid = getTotalPaidForInvoice(invoiceId);
        return invoice.getTotalAmount().subtract(totalPaid);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PaymentResponseDto updatePayment(Long id, PaymentUpdateDto dto) {
        log.info("Updating payment id: {}", id);

        Payment payment = paymentRepository.findNotDeletedById(id)
                .orElseThrow(() -> PaymentNotFoundException.byId(id));

        // Map update DTO to entity
        paymentMapper.updateEntity(dto, payment);

        Payment saved = paymentRepository.save(payment);
        log.info("Payment updated id: {}", saved.getId());

        return paymentMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deletePayment(Long id) {
        log.info("Soft-deleting payment id: {}", id);

        Payment payment = paymentRepository.findNotDeletedById(id)
                .orElseThrow(() -> PaymentNotFoundException.byId(id));

        Invoice invoice = payment.getInvoice();

        payment.softDelete(null);
        paymentRepository.save(payment);

        // Update invoice status
        updateInvoiceStatus(invoice);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean referenceNumberExists(String referenceNumber) {
        return paymentRepository.existsByReferenceNumber(referenceNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean receiptNumberExists(String receiptNumber) {
        return paymentRepository.existsByReceiptNumber(receiptNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean wouldExceedBalance(Long invoiceId, BigDecimal amount) {
        BigDecimal remainingBalance = getRemainingBalance(invoiceId);
        return amount.compareTo(remainingBalance) > 0;
    }

    // ══════════════════════════════════════════════════════════════════
    // Helper Methods
    // ═══════════════════════════════════════════════════════════════════

    private void updateInvoiceStatus(Invoice invoice) {
        BigDecimal totalPaid = getTotalPaidForInvoice(invoice.getId());
        BigDecimal totalAmount = invoice.getTotalAmount();

        if (totalPaid.compareTo(totalAmount) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        invoiceRepository.save(invoice);
    }
}
