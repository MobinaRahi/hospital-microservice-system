package hospital.authservice.model.enums;

/**
 * Subscription plan types for the SaaS platform.
 * Each plan determines the features, limits, and pricing for a tenant (hospital).
 *
 * @author MobinaRahi
 */
public enum PlanType {
    /**
     * Free trial — 14 days, limited features.
     */
    TRIAL,

    /**
     * Basic plan — Multi-tenant, up to 50 patients/month, 5 core modules.
     */
    BASIC,

    /**
     * Professional plan — Multi-tenant, unlimited patients, all 8 modules.
     */
    PROFESSIONAL,

    /**
     * Enterprise plan — Single-tenant (dedicated server), all modules + custom.
     */
    ENTERPRISE
}
