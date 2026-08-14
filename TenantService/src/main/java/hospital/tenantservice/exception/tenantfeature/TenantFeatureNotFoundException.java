package hospital.tenantservice.exception.tenantfeature;

/**
 * Exception thrown when a tenant feature is not found.
 *
 * @author MobinaRahi
 */
public class TenantFeatureNotFoundException extends RuntimeException {

    public TenantFeatureNotFoundException(String message) {
        super(message);
    }

    public static TenantFeatureNotFoundException byId(Long id) {
        return new TenantFeatureNotFoundException("Tenant feature with id " + id + " not found");
    }

    public static TenantFeatureNotFoundException byCode(Long tenantId, String featureCode) {
        return new TenantFeatureNotFoundException("Feature '" + featureCode + "' not found for tenant " + tenantId);
    }
}
