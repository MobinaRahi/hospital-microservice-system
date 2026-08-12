package hospital.billingservice.exception.payment;

/**
 * Exception thrown when a payment amount exceeds the remaining invoice balance.
 *
 * @author MobinaRahi
 */
public class PaymentExceedsBalanceException extends RuntimeException {

    public PaymentExceedsBalanceException(Long invoiceId, java.math.BigDecimal amount, java.math.BigDecimal balance) {
        super("Payment amount " + amount + " exceeds remaining balance " + balance + " for invoice " + invoiceId);
    }
}
