package hospital.inventoryservice.exception.supplier;

public class SupplierNotFoundException extends RuntimeException {

    public SupplierNotFoundException(String message) {
        super(message);
    }

    public static SupplierNotFoundException byId(Long id) {
        return new SupplierNotFoundException("Supplier with id " + id + " not found");
    }

    public static SupplierNotFoundException byEmail(String email) {
        return new SupplierNotFoundException("Supplier with email '" + email + "' not found");
    }
}
