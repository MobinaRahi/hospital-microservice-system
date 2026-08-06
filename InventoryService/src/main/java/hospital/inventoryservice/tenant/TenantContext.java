package hospital.inventoryservice.tenant;

/**
 * ThreadLocal holder for the current tenant ID.
 * Used to isolate data queries per tenant in a multi-tenant SaaS environment.
 *
 * <p><strong>Usage:</strong></p>
 * <ol>
 *   <li>Request arrives at API Gateway → tenant extracted from JWT or subdomain</li>
 *   <li>JwtAuthenticationFilter sets tenantId in TenantContext</li>
 *   <li>All database queries are automatically filtered by tenantId via Hibernate filter</li>
 *   <li>TenantContext is cleared at the end of request (TenantContextFilter)</li>
 * </ol>
 *
 * <p><strong>Thread Safety:</strong> Each HTTP request runs on its own thread,
 * so ThreadLocal ensures isolation between concurrent requests.</p>
 *
 * @author MobinaRahi
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Utility class — no instantiation
    }

    /**
     * Sets the tenant ID for the current thread.
     *
     * @param tenantId the ID of the current tenant
     */
    public static void setCurrentTenant(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Gets the tenant ID for the current thread.
     *
     * @return the current tenant ID, or null if not set
     */
    public static Long getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Clears the tenant ID from the current thread.
     * MUST be called at the end of each request to prevent memory leaks
     * and data leakage between requests in thread pools.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }

    /**
     * Checks whether a tenant ID is currently set.
     *
     * @return true if a tenant ID is set
     */
    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }
}
