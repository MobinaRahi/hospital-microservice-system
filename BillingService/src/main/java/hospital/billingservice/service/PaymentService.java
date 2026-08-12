package hospital.billingservice.service;

import hospital.billingservice.dto.payment.PaymentCreateDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.dto.payment.PaymentUpdateDto;
import hospital.billingservice.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Payment.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Payment amount cannot exceed the remaining invoice balance</li>
 *   <li>Multiple payments can be made against one invoice (partial payments)</li>
 *   <li>Registering a payment auto-updates the invoice status</li>
 *   <li>referenceNumber must be unique</li>
 *   <li>receiptNumber must be unique</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PaymentService {

    // ════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new payment record.
     * <p>Automatically updates the invoice status based on total payments.</p>
     *
     * @param dto the payment creation data
     * @return the created payment
     */
    PaymentResponseDto createPayment(PaymentCreateDto dto);

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═════════════════════════════════════════════════════════════════

    /**
     * Gets a payment by its ID.
     *
     * @param id the payment ID
     * @return the payment
     */
    PaymentResponseDto getPaymentById(Long id);

    /**
     * Gets all payments for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return list of payments for the invoice
     */
    List<PaymentResponseDto> getPaymentsByInvoice(Long invoiceId);

    /**
     * Gets payments by payment method.
     *
     * @param method the payment method
     * @return list of payments with the method
     */
    List<PaymentResponseDto> getPaymentsByMethod(PaymentMethod method);

    /**
     * Gets payments by reference number.
     *
     * @param referenceNumber the reference number
     * @return the payment
     */
    PaymentResponseDto getPaymentByReferenceNumber(String referenceNumber);

    /**
     * Gets payments by receipt number.
     *
     * @param receiptNumber the receipt number
     * @return the payment
     */
    PaymentResponseDto getPaymentByReceiptNumber(String receiptNumber);

    /**
     * Gets payments within a date range.
     * Useful for daily/weekly/monthly reports.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of payments in the range
     */
    List<PaymentResponseDto> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Gets the total amount paid for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return total amount paid
     */
    BigDecimal getTotalPaidForInvoice(Long invoiceId);

    /**
     * Gets the remaining balance for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return remaining balance (totalAmount - totalPaid)
     */
    BigDecimal getRemainingBalance(Long invoiceId);

    // ═════════════════════════════════════════════════════════════════
    // Update
    // ═════════════════════════════════════════════════════════════════

    /**
     * Updates an existing payment record.
     * <p>Only referenceNumber, receiptNumber, and notes can be updated.</p>
     *
     * @param id  the payment ID
     * @param dto the update data
     * @return the updated payment
     */
    PaymentResponseDto updatePayment(Long id, PaymentUpdateDto dto);

    // ════════════════════════════════════════════════════════════════
    // Delete
    // ═════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a payment record.
     * <p>Automatically updates the invoice status after deletion.</p>
     *
     * @param id the payment ID
     */
    void deletePayment(Long id);

    // ═════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════

    /**
     * Checks if a reference number is already in use.
     *
     * @param referenceNumber the reference number to check
     * @return true if the reference number exists
     */
    boolean referenceNumberExists(String referenceNumber);

    /**
     * Checks if a receipt number is already in use.
     *
     * @param receiptNumber the receipt number to check
     * @return true if the receipt number exists
     */
    boolean receiptNumberExists(String receiptNumber);

    /**
     * Checks if a payment amount would exceed the remaining invoice balance.
     *
     * @param invoiceId the invoice ID
     * @param amount    the payment amount
     * @return true if the amount would exceed the balance
     */
    boolean wouldExceedBalance(Long invoiceId, BigDecimal amount);
}
