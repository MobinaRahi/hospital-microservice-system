package hospital.billingservice.exception.payment;

/**
 * Exception thrown when a duplicate payment reference or receipt number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicatePaymentReferenceException extends RuntimeException {

    public DuplicatePaymentReferenceException(String field, String value) {
        super("Payment with " + field + " '" + value + "' already exists");
    }

    public static DuplicatePaymentReferenceException byReferenceNumber(String referenceNumber) {
        return new DuplicatePaymentReferenceException("reference number", referenceNumber);
    }

    public static DuplicatePaymentReferenceException byReceiptNumber(String receiptNumber) {
        return new DuplicatePaymentReferenceException("receipt number", receiptNumber);
    }
}
