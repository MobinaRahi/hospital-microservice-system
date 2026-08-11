package hospital.billingservice.tenant;

/**
 * ThreadLocal holder for the current tenant ID.
 * Used to isolate data queries per tenant in a multi-tenant SaaS environment.
 *
 * <p><strong>Usage:</strong></p>
 * <ol>
 *   <li>Request arrives → tenant extracted from JWT</li>
 *   <li>JwtAuthenticationFilter sets tenantId in TenantContext</li>
 *   <li>All database queries are automatically filtered by tenantId</li>
 *   <li>TenantContext is cleared at the end of request</li>
 * </ol>
 *
 * @author MobinaRahi
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setCurrentTenant(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static Long getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }
}
