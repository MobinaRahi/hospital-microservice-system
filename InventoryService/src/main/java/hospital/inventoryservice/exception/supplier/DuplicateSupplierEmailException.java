package hospital.inventoryservice.exception.supplier;

public class DuplicateSupplierEmailException extends RuntimeException {

    public DuplicateSupplierEmailException(String email) {
        super("Supplier with email '" + email + "' already exists");
    }
}
