package hospital.adminservice.tenant;

public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {}

    public static void setCurrentTenant(Long tenantId) { CURRENT_TENANT.set(tenantId); }
    public static Long getCurrentTenant() { return CURRENT_TENANT.get(); }
    public static void clear() { CURRENT_TENANT.remove(); }
    public static boolean hasTenant() { return CURRENT_TENANT.get() != null; }
}
