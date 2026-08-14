package hospital.tenantservice.tenant;

/**
 * ThreadLocal-based tenant context for multi-tenancy.
 *
 * <p>Stores the current tenant ID for the duration of the request.</p>
 *
 * @author MobinaRahi
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    /**
     * Gets the current tenant ID.
     *
     * @return the current tenant ID, or null if not set
     */
    public static Long getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Sets the current tenant ID.
     *
     * @param tenantId the tenant ID
     */
    public static void setCurrentTenant(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Clears the current tenant ID.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }

    /**
     * Checks if a tenant is currently set.
     *
     * @return true if tenant is set
     */
    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }
}
