package hospital.billingservice.exception.payment;

/**
 * Exception thrown when a payment is not found.
 *
 * @author MobinaRahi
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public static PaymentNotFoundException byId(Long id) {
        return new PaymentNotFoundException("Payment with id " + id + " not found");
    }

    public static PaymentNotFoundException byReferenceNumber(String referenceNumber) {
        return new PaymentNotFoundException("Payment with reference number '" + referenceNumber + "' not found");
    }
}
