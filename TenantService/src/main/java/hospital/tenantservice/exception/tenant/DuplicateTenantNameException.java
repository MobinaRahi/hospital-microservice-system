package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a duplicate tenant name is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateTenantNameException extends RuntimeException {

    public DuplicateTenantNameException(String name) {
        super("Tenant with name '" + name + "' already exists");
    }
}
