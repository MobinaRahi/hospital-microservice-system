package hospital.authservice.model.enums;

/**
 * Lifecycle status of a tenant (hospital) in the SaaS platform.
 *
 * @author MobinaRahi
 */
public enum TenantStatus {
    /**
     * New tenant in trial period (14 days free).
     */
    TRIAL,

    /**
     * Active, paying tenant with valid subscription.
     */
    ACTIVE,

    /**
     * Temporarily suspended (payment failed, abuse, etc.).
     */
    SUSPENDED,

    /**
     * Subscription has expired without renewal.
     */
    EXPIRED,

    /**
     * Tenant voluntarily cancelled their subscription.
     */
    CANCELLED
}
