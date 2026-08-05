package hospital.authservice.service;

import hospital.authservice.model.enums.PlanType;
import hospital.authservice.model.enums.TenantStatus;
import hospital.authservice.model.tenant.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for managing tenants (hospitals/clinics) in the SaaS platform.
 *
 * @author MobinaRahi
 */
public interface TenantService {

    /**
     * Registers a new tenant with a trial subscription.
     *
     * @param name       hospital/clinic name
     * @param subdomain  unique subdomain (e.g., "city-hospital")
     * @param adminEmail contact email for admin
     * @param phone      contact phone (optional)
     * @param address    physical address (optional)
     * @return the created tenant
     */
    Tenant registerTenant(String name, String subdomain, String adminEmail, String phone, String address);

    /**
     * Finds a tenant by its ID.
     */
    Tenant getTenantById(Long id);

    /**
     * Finds a tenant by its subdomain.
     */
    Tenant getTenantBySubdomain(String subdomain);

    /**
     * Finds a tenant by admin email.
     */
    Tenant getTenantByAdminEmail(String email);

    /**
     * Gets all tenants (paginated).
     */
    Page<Tenant> getAllTenants(Pageable pageable);

    /**
     * Gets tenants by status.
     */
    List<Tenant> getTenantsByStatus(TenantStatus status);

    /**
     * Gets tenants by plan type.
     */
    List<Tenant> getTenantsByPlanType(PlanType planType);

    /**
     * Updates tenant information.
     */
    Tenant updateTenant(Long id, String name, String phone, String address);

    /**
     * Upgrades or downgrades a tenant's subscription plan.
     */
    Tenant changePlan(Long tenantId, PlanType newPlan);

    /**
     * Suspends a tenant (e.g., payment failure).
     */
    Tenant suspendTenant(Long tenantId);

    /**
     * Reactivates a suspended tenant.
     */
    Tenant activateTenant(Long tenantId);

    /**
     * Soft-deletes a tenant.
     */
    void deleteTenant(Long tenantId);

    /**
     * Checks if a subdomain is available.
     */
    boolean isSubdomainAvailable(String subdomain);

    /**
     * Gets tenants with expiring subscriptions.
     */
    List<Tenant> getTenantsWithExpiringSubscription();

    /**
     * Counts active tenants.
     */
    long countActiveTenants();
}
