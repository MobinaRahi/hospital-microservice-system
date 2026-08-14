package hospital.tenantservice.repository;

import hospital.tenantservice.model.Tenant;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.TenantStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Tenant entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findBySubdomain - Find tenant by unique subdomain</li>
 *   <li>findByStatus - Find tenants by lifecycle status</li>
 *   <li>findByPlan - Find tenants by subscription plan</li>
 *   <li>findActiveTenants - Find all active and operational tenants</li>
 *   <li>findTenantsExpiringSoon - Find tenants with subscriptions expiring within N days</li>
 *   <li>countByStatus - Count tenants by status</li>
 *   <li>countByPlan - Count tenants by plan type</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface TenantRepository extends BaseEntityRepository<Tenant, Long> {

    /**
     * Finds a tenant by its unique subdomain.
     *
     * @param subdomain the subdomain to search for
     * @return Optional containing the tenant if found
     */
    Optional<Tenant> findBySubdomain(String subdomain);

    /**
     * Finds tenants by lifecycle status.
     *
     * @param status the tenant status (ACTIVE, SUSPENDED, INACTIVE, PENDING)
     * @return list of tenants with the specified status
     */
    List<Tenant> findByStatus(TenantStatus status);

    /**
     * Finds tenants by subscription plan type.
     *
     * @param plan the plan type (FREE, BASIC, PROFESSIONAL, ENTERPRISE)
     * @return list of tenants with the specified plan
     */
    List<Tenant> findByPlan(PlanType plan);

    /**
     * Finds tenants by plan and status.
     *
     * @param plan   the plan type
     * @param status the tenant status
     * @return list of matching tenants
     */
    List<Tenant> findByPlanAndStatus(PlanType plan, TenantStatus status);

    /**
     * Finds all active and operational tenants.
     *
     * @return list of active tenants
     */
    @Query("SELECT t FROM Tenant t WHERE t.isActive = true AND t.status = 'ACTIVE' AND t.deleted = false ORDER BY t.name")
    List<Tenant> findActiveTenants();

    /**
     * Finds tenants with subscriptions expiring within the specified number of days.
     *
     * @return list of tenants expiring soon
     */
    @Query("SELECT t FROM Tenant t WHERE t.endDate <= :expiryDate AND t.endDate >= :today AND t.isActive = true AND t.deleted = false")
    List<Tenant> findTenantsExpiringSoon(
            @Param("today") LocalDate today,
            @Param("expiryDate") LocalDate expiryDate);

    /**
     * Finds tenants with expired subscriptions.
     *
     * @param today current date
     * @return list of tenants with expired subscriptions
     */
    @Query("SELECT t FROM Tenant t WHERE t.endDate < :today AND t.isActive = true AND t.deleted = false")
    List<Tenant> findTenantsWithExpiredSubscriptions(@Param("today") LocalDate today);

    /**
     * Finds tenants that have reached their user limit.
     *
     * @return list of tenants at user capacity
     */
    @Query("SELECT t FROM Tenant t WHERE t.currentUsers >= t.maxUsers AND t.isActive = true AND t.deleted = false")
    List<Tenant> findTenantsAtUserCapacity();

    /**
     * Finds tenants that have reached their patient limit.
     *
     * @return list of tenants at patient capacity
     */
    @Query("SELECT t FROM Tenant t WHERE t.currentPatients >= t.maxPatients AND t.isActive = true AND t.deleted = false")
    List<Tenant> findTenantsAtPatientCapacity();

    /**
     * Searches tenants by name (case-insensitive).
     *
     * @param name the name pattern to search for
     * @return list of matching tenants
     */
    @Query("SELECT t FROM Tenant t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.deleted = false ORDER BY t.name")
    List<Tenant> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Counts tenants by status.
     *
     * @param status the tenant status
     * @return number of tenants with the status
     */
    long countByStatus(TenantStatus status);

    /**
     * Counts tenants by plan type.
     *
     * @param plan the plan type
     * @return number of tenants with the plan
     */
    long countByPlan(PlanType plan);

    /**
     * Checks if a subdomain is already taken.
     *
     * @param subdomain the subdomain to check
     * @return true if the subdomain exists
     */
    boolean existsBySubdomain(String subdomain);

    /**
     * Checks if a tenant name is already taken.
     *
     * @param name the name to check
     * @return true if the name exists
     */
    boolean existsByName(String name);
}
