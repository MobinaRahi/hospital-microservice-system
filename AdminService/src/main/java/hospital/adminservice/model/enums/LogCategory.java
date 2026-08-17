package hospital.adminservice.model.enums;

/**
 * Category of system log entries.
 *
 * @author MobinaRahi
 */
public enum LogCategory {

    /** Tenant management operations. */
    TENANT,

    /** Plan/subscription changes. */
    PLAN,

    /** User/admin management. */
    USER,

    /** Billing and revenue events. */
    BILLING,

    /** Security events (login, logout, failed attempts). */
    SECURITY,

    /** System events (startup, shutdown, config changes). */
    SYSTEM
}
