package hospital.inventoryservice.exception.purchaseorder;

public class PurchaseOrderNotFoundException extends RuntimeException {

    public PurchaseOrderNotFoundException(String message) {
        super(message);
    }

    public static PurchaseOrderNotFoundException byId(Long id) {
        return new PurchaseOrderNotFoundException("Purchase order with id " + id + " not found");
    }
}
