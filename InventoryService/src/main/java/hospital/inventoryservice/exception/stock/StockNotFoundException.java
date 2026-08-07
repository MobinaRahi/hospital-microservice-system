package hospital.inventoryservice.exception.stock;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String message) {
        super(message);
    }

    public static StockNotFoundException byId(Long id) {
        return new StockNotFoundException("Stock with id " + id + " not found");
    }

    public static StockNotFoundException byDrugId(Long drugId) {
        return new StockNotFoundException("Stock for drug with id " + drugId + " not found");
    }
}
