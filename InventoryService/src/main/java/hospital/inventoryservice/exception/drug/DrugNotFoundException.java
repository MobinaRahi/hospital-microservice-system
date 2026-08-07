package hospital.inventoryservice.exception.drug;

public class DrugNotFoundException extends RuntimeException {

    public DrugNotFoundException(String message) {
        super(message);
    }

    public static DrugNotFoundException byId(Long id) {
        return new DrugNotFoundException("Drug with id " + id + " not found");
    }

    public static DrugNotFoundException byBarcode(String barcode) {
        return new DrugNotFoundException("Drug with barcode '" + barcode + "' not found");
    }
}
