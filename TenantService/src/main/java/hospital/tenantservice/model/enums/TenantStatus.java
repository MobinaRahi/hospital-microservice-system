package hospital.tenantservice.model.enums;

/**
 * Lifecycle status of a tenant (hospital/clinic).
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → ACTIVE → SUSPENDED → ACTIVE (reactivated)
 *                 ↓
 *               INACTIVE (cancelled/expired)
 * </pre>
 *
 * <p><strong>Values:</strong></p>
 * <ul>
 *   <li>{@code PENDING} — Newly registered, awaiting admin approval</li>
 *   <li>{@code ACTIVE} — Fully operational tenant</li>
 *   <li>{@code SUSPENDED} — Temporarily suspended (payment issue, violation)</li>
 *   <li>{@code INACTIVE} — Permanently inactive (expired, cancelled)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum TenantStatus {

    /** Newly registered tenant, awaiting admin approval or payment confirmation. */
    PENDING,

    /** Fully operational tenant with active subscription. */
    ACTIVE,

    /** Temporarily suspended tenant (payment overdue, policy violation). */
    SUSPENDED,

    /** Permanently inactive tenant (subscription expired, voluntarily cancelled). */
    INACTIVE
}
