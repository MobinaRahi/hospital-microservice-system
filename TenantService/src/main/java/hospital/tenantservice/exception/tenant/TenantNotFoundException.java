package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a tenant is not found.
 *
 * @author MobinaRahi
 */
public class TenantNotFoundException extends RuntimeException {

    public TenantNotFoundException(String message) {
        super(message);
    }

    public static TenantNotFoundException byId(Long id) {
        return new TenantNotFoundException("Tenant with id " + id + " not found");
    }

    public static TenantNotFoundException bySubdomain(String subdomain) {
        return new TenantNotFoundException("Tenant with subdomain '" + subdomain + "' not found");
    }
}
