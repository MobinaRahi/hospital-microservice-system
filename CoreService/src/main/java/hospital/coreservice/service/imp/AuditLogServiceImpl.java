package hospital.coreservice.service.imp;

import hospital.coreservice.dto.audit_log.AuditLogResponseDto;
import hospital.coreservice.mapper.AuditLogMapper;
import hospital.coreservice.model.AuditLog;
import hospital.coreservice.model.User;
import hospital.coreservice.model.enums.AuditStatus;
import hospital.coreservice.repository.AuditLogRepository;
import hospital.coreservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of AuditLogService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    // ========== Create ==========

    @Override
    @Async
    @Transactional
    public void logSuccess(User user, String action, String newValue, Long duration) {
        AuditLog auditLog = AuditLog.success(user, action, newValue, duration);
        auditLogRepository.save(auditLog);
        log.debug("Audit log SUCCESS: user={}, action={}, duration={}ms", user.getFullName(), action, duration);
    }

    @Override
    @Async
    @Transactional
    public void logFailure(User user, String action, String errorMessage, Long duration) {
        AuditLog auditLog = AuditLog.failure(user, action, errorMessage, duration);
        auditLogRepository.save(auditLog);
        log.warn("Audit log FAILURE: user={}, action={}, error={}", user.getFullName(), action, errorMessage);
    }

    @Override
    @Async
    @Transactional
    public void logWarning(User user, String action, String message, Long duration) {
        AuditLog auditLog = AuditLog.warning(user, action, message, duration);
        auditLogRepository.save(auditLog);
        log.warn("Audit log WARNING: user={}, action={}, message={}", user.getFullName(), action, message);
    }

    @Override
    @Async
    @Transactional
    public void log(User user, String userName, String action, String oldValue,
                    String newValue, AuditStatus status, String errorMessage, Long duration) {
        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .userName(userName)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .status(status)
                .errorMessage(errorMessage)
                .duration(duration)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(auditLog);
        log.debug("Audit log saved: user={}, action={}, status={}", userName, action, status);
    }

    // ========== Find by User ==========

    @Override
    public List<AuditLogResponseDto> getLogsByUserId(Long userId) {
        log.debug("Fetching audit logs for user: {}", userId);
        return auditLogRepository.findByUserId(userId)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsByUserIdOrderByTimestampDesc(Long userId) {
        log.debug("Fetching audit logs for user: {} ordered by timestamp desc", userId);
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsByUserName(String userName) {
        log.debug("Fetching audit logs for username: {}", userName);
        return auditLogRepository.findByUserName(userName)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Find by Status ==========

    @Override
    public List<AuditLogResponseDto> getLogsByStatus(AuditStatus status) {
        log.debug("Fetching audit logs by status: {}", status);
        return auditLogRepository.findByStatus(status)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsByStatusOrderByTimestampDesc(AuditStatus status) {
        log.debug("Fetching audit logs by status: {} ordered by timestamp desc", status);
        return auditLogRepository.findByStatusOrderByTimestampDesc(status)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getFailedAndWarningLogs() {
        log.debug("Fetching failed and warning audit logs");
        return auditLogRepository.findFailedAndWarningLogs()
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Find by Action ==========

    @Override
    public List<AuditLogResponseDto> getLogsByActionContaining(String action) {
        log.debug("Fetching audit logs by action containing: {}", action);
        return auditLogRepository.findByActionContainingIgnoreCase(action)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsByActionAndStatus(String action, AuditStatus status) {
        log.debug("Fetching audit logs by action: {} and status: {}", action, status);
        return auditLogRepository.findByActionContainingIgnoreCaseAndStatus(action, status)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Date Range ==========

    @Override
    public List<AuditLogResponseDto> getLogsBetween(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching audit logs between: {} and {}", start, end);
        return auditLogRepository.findByTimestampBetween(start, end)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsAfter(LocalDateTime timestamp) {
        log.debug("Fetching audit logs after: {}", timestamp);
        return auditLogRepository.findByTimestampAfter(timestamp)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsBefore(LocalDateTime timestamp) {
        log.debug("Fetching audit logs before: {}", timestamp);
        return auditLogRepository.findByTimestampBefore(timestamp)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getTodayLogs(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        log.debug("Fetching today's audit logs");
        return auditLogRepository.findTodayLogs(startOfDay, endOfDay)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsFromLastDays(int days) {
        log.debug("Fetching audit logs from last {} days", days);
        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
        return auditLogRepository.findLogsFromLastDays(fromDate)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Find by Duration ==========

    @Override
    public List<AuditLogResponseDto> getLogsWithDurationGreaterThan(Long duration) {
        log.debug("Fetching audit logs with duration > {}ms", duration);
        return auditLogRepository.findByDurationGreaterThan(duration)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponseDto> getLogsWithDurationGreaterThanOrderByDurationDesc(Long duration) {
        log.debug("Fetching audit logs with duration > {}ms ordered by duration desc", duration);
        return auditLogRepository.findByDurationGreaterThanOrderByDurationDesc(duration)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Advanced Search ==========

    @Override
    public List<AuditLogResponseDto> searchAuditLogs(Long userId, AuditStatus status, String action,
                                                     LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Searching audit logs with filters: userId={}, status={}, action={}, start={}, end={}",
                userId, status, action, startDate, endDate);
        return auditLogRepository.searchAuditLogs(userId, status, action, startDate, endDate)
                .stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Statistics ==========

    @Override
    public long countLogsByStatus(AuditStatus status) {
        log.debug("Counting audit logs by status: {}", status);
        return auditLogRepository.countByStatus(status);
    }

    @Override
    public long countLogsByUserId(Long userId) {
        log.debug("Counting audit logs by user: {}", userId);
        return auditLogRepository.countByUserId(userId);
    }

    @Override
    public long countTodayLogs() {
        log.debug("Counting today's audit logs");
        return auditLogRepository.countTodayLogs();
    }

    @Override
    public Map<String, Map<String, Long>> getLogsCountByDateAndStatus(int lastDays) {
        log.debug("Getting logs count by date and status for last {} days", lastDays);
        LocalDateTime fromDate = LocalDateTime.now().minusDays(lastDays);
        List<Object[]> results = auditLogRepository.countLogsByDateAndStatus(fromDate);

        return results.stream()
                .collect(Collectors.groupingBy(
                        row -> row[0].toString(),
                        Collectors.toMap(
                                row -> row[1].toString(),
                                row -> (Long) row[2],
                                Long::sum
                        )
                ));
    }

    @Override
    public Long getAverageSuccessDuration() {
        log.debug("Getting average success duration");
        return auditLogRepository.getAverageSuccessDuration();
    }

    @Override
    public Long getAverageFailureDuration() {
        log.debug("Getting average failure duration");
        return auditLogRepository.getAverageFailureDuration();
    }

    // ========== Cleanup ==========

    @Override
    @Transactional
    public void deleteLogsOlderThan(LocalDateTime cutoffDate) {
        log.info("Deleting audit logs older than: {}", cutoffDate);
        long deleted = auditLogRepository.deleteLogsOlderThan(cutoffDate);
        log.info("Deleted {} audit logs older than {}", deleted, cutoffDate);
    }

    @Override
    @Transactional
    public void archiveOldLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        log.info("Archiving audit logs older than {} days (cutoff: {})", daysToKeep, cutoffDate);
        long deleted = auditLogRepository.deleteLogsOlderThan(cutoffDate);
        log.info("Archived {} audit logs", deleted);
    }

    // ========== Existence Checks ==========

    @Override
    public boolean existsLogsByUserId(Long userId) {
        log.debug("Checking if audit logs exist for user: {}", userId);
        return auditLogRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsFailedLogsByUserId(Long userId) {
        log.debug("Checking if failed audit logs exist for user: {}", userId);
        return auditLogRepository.existsFailedLogsByUserId(userId);
    }
}