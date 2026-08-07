package hospital.inventoryservice.exception.purchaseorder;

public class IllegalStatusTransitionException extends RuntimeException {

    public IllegalStatusTransitionException(String message) {
        super(message);
    }

    public static IllegalStatusTransitionException fromTo(String from, String to) {
        return new IllegalStatusTransitionException("Cannot transition purchase order status from '" + from + "' to '" + to + "'");
    }

    public static IllegalStatusTransitionException cannotCancelCompleted() {
        return new IllegalStatusTransitionException("Cannot cancel a completed purchase order");
    }

    public static IllegalStatusTransitionException canOnlyAddToPending() {
        return new IllegalStatusTransitionException("Can only add items to PENDING purchase orders");
    }
}
