package hospital.billingservice.repository;

import hospital.billingservice.model.Payment;
import hospital.billingservice.model.enums.PaymentMethod;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Payment entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface PaymentRepository extends BaseEntityRepository<Payment, Long> {

    /**
     * Finds all payments for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return list of payments for the invoice
     */
    List<Payment> findByInvoiceId(Long invoiceId);

    /**
     * Finds payments by payment method.
     *
     * @param method the payment method
     * @return list of payments with the method
     */
    List<Payment> findByMethod(PaymentMethod method);

    /**
     * Finds payments by reference number.
     *
     * @param referenceNumber the reference number from payment gateway
     * @return payment if found
     */
    Optional<Payment> findByReferenceNumber(String referenceNumber);

    /**
     * Finds payments by receipt number.
     *
     * @param receiptNumber the internal receipt number
     * @return payment if found
     */
    Optional<Payment> findByReceiptNumber(String receiptNumber);

    /**
     * Finds payments within a date range.
     * Useful for daily/weekly/monthly reports.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of payments in the range
     */
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds payments received by a specific user.
     *
     * @param receivedBy the user ID who received the payment
     * @return list of payments received by the user
     */
    List<Payment> findByReceivedBy(Long receivedBy);

    /**
     * Checks if a reference number already exists.
     *
     * @param referenceNumber the reference number to check
     * @return true if exists
     */
    boolean existsByReferenceNumber(String referenceNumber);

    /**
     * Checks if a receipt number already exists.
     *
     * @param receiptNumber the receipt number to check
     * @return true if exists
     */
    boolean existsByReceiptNumber(String receiptNumber);
}
