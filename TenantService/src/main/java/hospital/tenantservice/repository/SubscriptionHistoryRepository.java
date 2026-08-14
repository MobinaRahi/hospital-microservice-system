package hospital.tenantservice.repository;

import hospital.tenantservice.model.SubscriptionHistory;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for SubscriptionHistory entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByTenantId - Find all history for a tenant</li>
 *   <li>findByTenantIdOrderByChangeDateDesc - Find history sorted by date</li>
 *   <li>findByChangeType - Find all changes of a specific type</li>
 *   <li>findByNewPlan - Find all upgrades/downgrades to a specific plan</li>
 *   <li>findByEffectiveDateBetween - Find scheduled changes in a date range</li>
 *   <li>countByChangeType - Count changes by type</li>
 *   <li>countByTenantId - Count changes for a tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface SubscriptionHistoryRepository extends BaseEntityRepository<SubscriptionHistory, Long> {

    /**
     * Finds all subscription changes for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return list of subscription history records
     */
    List<SubscriptionHistory> findByTenantId(Long tenantId);

    /**
     * Finds subscription history for a tenant, ordered by change date (newest first).
     *
     * @param tenantId the tenant ID
     * @return list of subscription history records sorted by date
     */
    List<SubscriptionHistory> findByTenantIdOrderByChangeDateDesc(Long tenantId);

    /**
     * Finds all subscription changes of a specific type.
     *
     * @param changeType the change type (UPGRADED, DOWNGRADED, ACTIVATED, etc.)
     * @return list of subscription history records with the change type
     */
    List<SubscriptionHistory> findByChangeType(SubscriptionChangeType changeType);

    /**
     * Finds all upgrades to a specific plan.
     *
     * @param newPlan the new plan type
     * @return list of upgrade records
     */
    List<SubscriptionHistory> findByNewPlan(PlanType newPlan);

    /**
     * Finds scheduled changes with effective dates in a specific range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of scheduled changes
     */
    @Query("SELECT h FROM SubscriptionHistory h WHERE h.effectiveDate BETWEEN :startDate AND :endDate AND h.deleted = false ORDER BY h.effectiveDate")
    List<SubscriptionHistory> findByEffectiveDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Finds pending scheduled changes (effective date in the future).
     *
     * @param today current date
     * @return list of pending scheduled changes
     */
    @Query("SELECT h FROM SubscriptionHistory h WHERE h.effectiveDate > :today AND h.deleted = false ORDER BY h.effectiveDate")
    List<SubscriptionHistory> findPendingScheduledChanges(@Param("today") LocalDate today);

    /**
     * Finds all changes made by a specific user (admin).
     *
     * @param changedBy the user ID who made the change
     * @return list of changes made by the user
     */
    List<SubscriptionHistory> findByChangedBy(Long changedBy);

    /**
     * Counts subscription changes by type.
     *
     * @param changeType the change type
     * @return number of changes of the type
     */
    long countByChangeType(SubscriptionChangeType changeType);

    /**
     * Counts subscription changes for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return number of changes for the tenant
     */
    long countByTenantId(Long tenantId);

    /**
     * Finds the most recent change for a tenant.
     *
     * @param tenantId the tenant ID
     * @return Optional containing the most recent change
     */
    @Query("SELECT h FROM SubscriptionHistory h WHERE h.tenantId = :tenantId AND h.deleted = false ORDER BY h.changeDate DESC")
    List<SubscriptionHistory> findMostRecentChange(@Param("tenantId") Long tenantId);
}
