package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a tenant cannot be suspended.
 *
 * @author MobinaRahi
 */
public class TenantSuspensionException extends RuntimeException {

    public TenantSuspensionException(String message) {
        super(message);
    }

    public static TenantSuspensionException cannotSuspend(String status) {
        return new TenantSuspensionException("Cannot suspend tenant in status: " + status);
    }
}
