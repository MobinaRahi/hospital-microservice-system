package hospital.tenantservice.service;

import hospital.tenantservice.dto.tenant.*;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.TenantStatus;

import java.util.List;

/**
 * Service interface for Tenant (hospital/clinic) management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each tenant must have a unique subdomain</li>
 *   <li>New tenants start with PENDING status</li>
 *   <li>Plan limits are enforced on user/patient/appointment creation</li>
 *   <li>Subscriptions can be upgraded, downgraded, suspended, or cancelled</li>
 *   <li>Tenant data is isolated by tenantId for multi-tenancy</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface TenantService {

    /**
     * Registers a new tenant (hospital/clinic).
     * Initial status is PENDING.
     *
     * @param dto the tenant creation data
     * @return the created tenant
     */
    TenantResponseDto createTenant(TenantCreateDto dto);

    /**
     * Gets a tenant by its ID.
     *
     * @param id the tenant ID
     * @return the tenant
     */
    TenantResponseDto getTenantById(Long id);

    /**
     * Gets a tenant by its unique subdomain.
     *
     * @param subdomain the subdomain
     * @return the tenant
     */
    TenantResponseDto getTenantBySubdomain(String subdomain);

    /**
     * Gets all tenants.
     *
     * @return list of all tenants
     */
    List<TenantResponseDto> getAllTenants();

    /**
     * Gets tenants by status.
     *
     * @param status the tenant status
     * @return list of tenants with the status
     */
    List<TenantResponseDto> getTenantsByStatus(TenantStatus status);

    /**
     * Gets tenants by subscription plan.
     *
     * @param plan the subscription plan
     * @return list of tenants with the plan
     */
    List<TenantResponseDto> getTenantsByPlan(PlanType plan);

    /**
     * Gets active and operational tenants.
     *
     * @return list of active tenants
     */
    List<TenantResponseDto> getActiveTenants();

    /**
     * Searches tenants by name (case-insensitive).
     *
     * @param name the name pattern
     * @return list of matching tenants
     */
    List<TenantResponseDto> searchTenantsByName(String name);

    /**
     * Gets tenants with subscriptions expiring within the specified days.
     *
     * @param days number of days from now
     * @return list of tenants expiring soon
     */
    List<TenantResponseDto> getTenantsExpiringWithin(int days);

    /**
     * Updates an existing tenant.
     *
     * @param id  the tenant ID
     * @param dto the update data
     * @return the updated tenant
     */
    TenantResponseDto updateTenant(Long id, TenantUpdateDto dto);

    /**
     * Activates a tenant (PENDING/SUSPENDED → ACTIVE).
     *
     * @param id  the tenant ID
     * @param dto status update data (reason, notes)
     * @return the activated tenant
     */
    TenantResponseDto activateTenant(Long id, TenantStatusUpdateDto dto);

    /**
     * Deactivates a tenant (→ INACTIVE).
     *
     * @param id  the tenant ID
     * @param dto status update data (reason, notes)
     * @return the deactivated tenant
     */
    TenantResponseDto deactivateTenant(Long id, TenantStatusUpdateDto dto);

    /**
     * Suspends a tenant temporarily (ACTIVE → SUSPENDED).
     *
     * @param id  the tenant ID
     * @param dto status update data (reason, notes)
     * @return the suspended tenant
     */
    TenantResponseDto suspendTenant(Long id, TenantStatusUpdateDto dto);

    /**
     * Upgrades the tenant subscription plan.
     *
     * @param id  the tenant ID
     * @param dto plan change data (newPlan, effectiveDate, reason)
     * @return the updated tenant
     */
    TenantResponseDto upgradeTenant(Long id, TenantPlanChangeDto dto);

    /**
     * Downgrades the tenant subscription plan.
     * Verifies current usage doesn't exceed new limits.
     *
     * @param id  the tenant ID
     * @param dto plan change data (newPlan, effectiveDate, reason)
     * @return the updated tenant
     */
    TenantResponseDto downgradeTenant(Long id, TenantPlanChangeDto dto);

    /**
     * Gets usage statistics for a tenant.
     *
     * @param id the tenant ID
     * @return usage statistics
     */
    TenantUsageResponseDto getTenantUsage(Long id);

    /**
     * Soft-deletes a tenant.
     *
     * @param id the tenant ID
     */
    void deleteTenant(Long id);

    /**
     * Checks if a subdomain is already taken.
     *
     * @param subdomain the subdomain to check
     * @return true if exists
     */
    boolean subdomainExists(String subdomain);

    /**
     * Counts tenants by status.
     *
     * @param status the tenant status
     * @return number of tenants with the status
     */
    long countTenantsByStatus(TenantStatus status);

    /**
     * Counts tenants by plan type.
     *
     * @param plan the plan type
     * @return number of tenants with the plan
     */
    long countTenantsByPlan(PlanType plan);
}
