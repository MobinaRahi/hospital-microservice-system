package hospital.tenantservice.exception.tenantfeature;

/**
 * Exception thrown when a duplicate tenant feature is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateTenantFeatureException extends RuntimeException {

    public DuplicateTenantFeatureException(String message) {
        super(message);
    }

    public static DuplicateTenantFeatureException forTenant(Long tenantId, String featureCode) {
        return new DuplicateTenantFeatureException("Feature '" + featureCode + "' already exists for tenant " + tenantId);
    }
}
