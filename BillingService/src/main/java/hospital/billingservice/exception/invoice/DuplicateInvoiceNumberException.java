package hospital.billingservice.exception.invoice;

/**
 * Exception thrown when a duplicate invoice number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateInvoiceNumberException extends RuntimeException {

    public DuplicateInvoiceNumberException(String invoiceNumber) {
        super("Invoice with number '" + invoiceNumber + "' already exists");
    }
}
