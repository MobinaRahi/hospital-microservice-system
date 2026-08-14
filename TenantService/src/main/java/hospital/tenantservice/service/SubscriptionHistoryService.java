package hospital.tenantservice.service;

import hospital.tenantservice.dto.subscriptionhistory.SubscriptionHistoryResponseDto;
import hospital.tenantservice.model.enums.SubscriptionChangeType;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Subscription History (audit trail).
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Every plan change, activation, suspension, or cancellation is recorded</li>
 *   <li>History is read-only (no updates or deletes)</li>
 *   <li>Scheduled changes (future effectiveDate) are tracked separately</li>
 *   <li>Used for billing calculations and compliance audit</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface SubscriptionHistoryService {

    /**
     * Gets the full subscription history for a tenant.
     * Ordered by change date (newest first).
     *
     * @param tenantId the tenant ID
     * @return list of subscription history records
     */
    List<SubscriptionHistoryResponseDto> getHistoryByTenant(Long tenantId);

    /**
     * Gets the most recent change for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the most recent change (or null if none)
     */
    SubscriptionHistoryResponseDto getMostRecentChange(Long tenantId);

    /**
     * Gets history filtered by change type.
     *
     * @param changeType the change type (UPGRADED, DOWNGRADED, etc.)
     * @return list of matching history records
     */
    List<SubscriptionHistoryResponseDto> getHistoryByChangeType(SubscriptionChangeType changeType);

    /**
     * Gets all upgrades to a specific plan.
     *
     * @param newPlan the new plan type
     * @return list of upgrade records
     */
    List<SubscriptionHistoryResponseDto> getUpgradesToPlan(SubscriptionChangeType newPlan);

    /**
     * Gets scheduled changes within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of scheduled changes
     */
    List<SubscriptionHistoryResponseDto> getScheduledChanges(LocalDate startDate, LocalDate endDate);

    /**
     * Gets pending scheduled changes (effective date in the future).
     *
     * @return list of pending scheduled changes
     */
    List<SubscriptionHistoryResponseDto> getPendingScheduledChanges();

    /**
     * Gets all changes made by a specific admin user.
     *
     * @param userId the user ID who made the changes
     * @return list of changes made by the user
     */
    List<SubscriptionHistoryResponseDto> getChangesByUser(Long userId);

    /**
     * Counts changes by type (for statistics).
     *
     * @param changeType the change type
     * @return number of changes of the type
     */
    long countByChangeType(SubscriptionChangeType changeType);

    /**
     * Counts total changes for a tenant.
     *
     * @param tenantId the tenant ID
     * @return number of changes for the tenant
     */
    long countByTenant(Long tenantId);
}
