package hospital.billingservice.exception.servicecatalog;

/**
 * Exception thrown when a duplicate service code is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateServiceCodeException extends RuntimeException {

    public DuplicateServiceCodeException(String code) {
        super("Service with code '" + code + "' already exists");
    }
}
