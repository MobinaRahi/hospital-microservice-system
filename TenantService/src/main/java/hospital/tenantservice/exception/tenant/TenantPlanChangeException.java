package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a tenant plan change (upgrade/downgrade) fails.
 *
 * @author MobinaRahi
 */
public class TenantPlanChangeException extends RuntimeException {

    public TenantPlanChangeException(String message) {
        super(message);
    }

    public static TenantPlanChangeException cannotDowngrade(String reason) {
        return new TenantPlanChangeException("Cannot downgrade plan: " + reason);
    }

    public static TenantPlanChangeException invalidPlanChange(String currentPlan, String newPlan) {
        return new TenantPlanChangeException("Cannot change from " + currentPlan + " to " + newPlan);
    }
}
