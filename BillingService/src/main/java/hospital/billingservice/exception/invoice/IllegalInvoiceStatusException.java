package hospital.billingservice.exception.invoice;

/**
 * Exception thrown for illegal invoice status transitions.
 *
 * @author MobinaRahi
 */
public class IllegalInvoiceStatusException extends RuntimeException {

    public IllegalInvoiceStatusException(String message) {
        super(message);
    }

    public static IllegalInvoiceStatusException cannotCancel(String currentStatus) {
        return new IllegalInvoiceStatusException("Cannot cancel invoice in status: " + currentStatus);
    }

    public static IllegalInvoiceStatusException cannotMarkPaid(String currentStatus) {
        return new IllegalInvoiceStatusException("Cannot mark as paid from status: " + currentStatus);
    }
}
