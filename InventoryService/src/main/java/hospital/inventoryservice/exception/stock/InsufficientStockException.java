package hospital.inventoryservice.exception.stock;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long stockId, int requested, int available) {
        super("Insufficient stock for stock id " + stockId + ". Requested: " + requested + ", Available: " + available);
    }
}
