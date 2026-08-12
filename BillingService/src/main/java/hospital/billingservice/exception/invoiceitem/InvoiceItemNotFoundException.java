package hospital.billingservice.exception.invoiceitem;

/**
 * Exception thrown when an invoice item is not found.
 *
 * @author MobinaRahi
 */
public class InvoiceItemNotFoundException extends RuntimeException {

    public InvoiceItemNotFoundException(String message) {
        super(message);
    }

    public static InvoiceItemNotFoundException byId(Long id) {
        return new InvoiceItemNotFoundException("Invoice item with id " + id + " not found");
    }
}
