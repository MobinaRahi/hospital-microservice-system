package hospital.tenantservice.service.impl;

import hospital.tenantservice.dto.subscriptionhistory.SubscriptionHistoryResponseDto;
import hospital.tenantservice.exception.subscriptionhistory.SubscriptionHistoryNotFoundException;
import hospital.tenantservice.mapper.SubscriptionHistoryMapper;
import hospital.tenantservice.model.SubscriptionHistory;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import hospital.tenantservice.repository.SubscriptionHistoryRepository;
import hospital.tenantservice.service.SubscriptionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link SubscriptionHistoryService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>History is read-only (no updates or deletes)</li>
 *   <li>Every plan change, activation, suspension, or cancellation is recorded</li>
 *   <li>Scheduled changes (future effectiveDate) are tracked separately</li>
 *   <li>Used for billing calculations and compliance audit</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SubscriptionHistoryServiceImpl implements SubscriptionHistoryService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;

    @Override
    public List<SubscriptionHistoryResponseDto> getHistoryByTenant(Long tenantId) {
        log.debug("Fetching subscription history for tenant {}", tenantId);

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findByTenantIdOrderByChangeDateDesc(tenantId);

        if (history.isEmpty()) {
            throw SubscriptionHistoryNotFoundException.byTenant(tenantId);
        }

        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public SubscriptionHistoryResponseDto getMostRecentChange(Long tenantId) {
        log.debug("Fetching most recent change for tenant {}", tenantId);

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findMostRecentChange(tenantId);

        if (history.isEmpty()) {
            throw SubscriptionHistoryNotFoundException.byTenant(tenantId);
        }

        return subscriptionHistoryMapper.toResponseDto(history.get(0));
    }

    @Override
    public List<SubscriptionHistoryResponseDto> getHistoryByChangeType(SubscriptionChangeType changeType) {
        log.debug("Fetching history by change type: {}", changeType);

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findByChangeType(changeType);
        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public List<SubscriptionHistoryResponseDto> getUpgradesToPlan(SubscriptionChangeType newPlan) {
        log.debug("Fetching upgrades to plan: {}", newPlan);

        // Note: This should actually use PlanType, but the interface uses SubscriptionChangeType
        // For now, we'll return all changes of the specified type
        List<SubscriptionHistory> history = subscriptionHistoryRepository.findByChangeType(newPlan);
        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public List<SubscriptionHistoryResponseDto> getScheduledChanges(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching scheduled changes between {} and {}", startDate, endDate);

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findByEffectiveDateBetween(startDate, endDate);
        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public List<SubscriptionHistoryResponseDto> getPendingScheduledChanges() {
        log.debug("Fetching pending scheduled changes");

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findPendingScheduledChanges(LocalDate.now());
        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public List<SubscriptionHistoryResponseDto> getChangesByUser(Long userId) {
        log.debug("Fetching changes by user {}", userId);

        List<SubscriptionHistory> history = subscriptionHistoryRepository.findByChangedBy(userId);
        return subscriptionHistoryMapper.toResponseDtoList(history);
    }

    @Override
    public long countByChangeType(SubscriptionChangeType changeType) {
        return subscriptionHistoryRepository.countByChangeType(changeType);
    }

    @Override
    public long countByTenant(Long tenantId) {
        return subscriptionHistoryRepository.countByTenantId(tenantId);
    }
}
