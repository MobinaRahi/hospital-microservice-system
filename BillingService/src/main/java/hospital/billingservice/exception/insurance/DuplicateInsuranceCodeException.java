package hospital.billingservice.exception.insurance;

/**
 * Exception thrown when a duplicate insurance code is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateInsuranceCodeException extends RuntimeException {

    public DuplicateInsuranceCodeException(String code) {
        super("Insurance plan with code '" + code + "' already exists");
    }
}
