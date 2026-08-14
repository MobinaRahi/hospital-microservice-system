package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a duplicate subdomain is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateSubdomainException extends RuntimeException {

    public DuplicateSubdomainException(String subdomain) {
        super("Subdomain '" + subdomain + "' is already taken");
    }
}
