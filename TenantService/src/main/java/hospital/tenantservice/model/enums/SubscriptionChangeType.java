package hospital.tenantservice.model.enums;

/**
 * Types of subscription changes for audit trail.
 *
 * <p><strong>Values:</strong></p>
 * <ul>
 *   <li>{@code CREATED} - New tenant registration</li>
 *   <li>{@code UPGRADED} - Plan upgrade to higher tier</li>
 *   <li>{@code DOWNGRADED} - Plan downgrade to lower tier</li>
 *   <li>{@code ACTIVATED} - Tenant activated from pending/suspended</li>
 *   <li>{@code SUSPENDED} - Tenant temporarily suspended</li>
 *   <li>{@code REACTIVATED} - Tenant reactivated after suspension</li>
 *   <li>{@code CANCELLED} - Subscription voluntarily cancelled</li>
 *   <li>{@code EXPIRED} - Subscription expired (end date reached)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum SubscriptionChangeType {

    /** New tenant registration. */
    CREATED,

    /** Plan upgrade to higher tier. */
    UPGRADED,

    /** Plan downgrade to lower tier. */
    DOWNGRADED,

    /** Tenant activated from pending or suspended status. */
    ACTIVATED,

    /** Tenant temporarily suspended (payment issue, violation). */
    SUSPENDED,

    /** Tenant reactivated after suspension. */
    REACTIVATED,

    /** Subscription voluntarily cancelled by tenant. */
    CANCELLED,

    /** Subscription expired (end date reached). */
    EXPIRED
}
