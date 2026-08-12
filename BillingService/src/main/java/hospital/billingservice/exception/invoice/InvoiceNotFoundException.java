package hospital.billingservice.exception.invoice;

/**
 * Exception thrown when an invoice is not found.
 *
 * @author MobinaRahi
 */
public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }

    public static InvoiceNotFoundException byId(Long id) {
        return new InvoiceNotFoundException("Invoice with id " + id + " not found");
    }

    public static InvoiceNotFoundException byNumber(String invoiceNumber) {
        return new InvoiceNotFoundException("Invoice with number '" + invoiceNumber + "' not found");
    }
}
