package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a tenant cannot be activated.
 *
 * @author MobinaRahi
 */
public class TenantActivationException extends RuntimeException {

    public TenantActivationException(String message) {
        super(message);
    }

    public static TenantActivationException cannotActivate(String status) {
        return new TenantActivationException("Cannot activate tenant in status: " + status);
    }
}
