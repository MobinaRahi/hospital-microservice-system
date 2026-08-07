package hospital.inventoryservice.exception.purchaseorderitem;

public class PurchaseOrderItemNotFoundException extends RuntimeException {

    public PurchaseOrderItemNotFoundException(String message) {
        super(message);
    }

    public static PurchaseOrderItemNotFoundException byId(Long id) {
        return new PurchaseOrderItemNotFoundException("Purchase order item with id " + id + " not found");
    }
}
