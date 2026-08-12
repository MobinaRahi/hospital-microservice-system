package hospital.billingservice.exception.invoiceitem;

/**
 * Exception thrown when trying to add an item to a non-PENDING invoice.
 *
 * @author MobinaRahi
 */
public class CannotAddItemToInvoiceException extends RuntimeException {

    public CannotAddItemToInvoiceException(Long invoiceId, String status) {
        super("Cannot add items to invoice " + invoiceId + " with status: " + status + ". Only PENDING invoices can have items added.");
    }
}
