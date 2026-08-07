package hospital.inventoryservice.exception.drug;

public class DuplicateDrugBarcodeException extends RuntimeException {

    public DuplicateDrugBarcodeException(String barcode) {
        super("Drug with barcode '" + barcode + "' already exists");
    }
}
