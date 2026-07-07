package hospital.authservice.service;

import hospital.authservice.dto.audit_log.AuditLogResponseDto;
import hospital.authservice.model.User;
import hospital.authservice.model.enums.AuditStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AuditLogService {

    void logSuccess(User user, String action, String newValue, Long duration);

    void logFailure(User user, String action, String errorMessage, Long duration);

    void logWarning(User user, String action, String message, Long duration);

    void log(User user, String userName, String action, String oldValue,
             String newValue, AuditStatus status, String errorMessage, Long duration);

    List<AuditLogResponseDto> getLogsByUserId(Long userId);

    List<AuditLogResponseDto> getLogsByUserIdOrderByTimestampDesc(Long userId);

    List<AuditLogResponseDto> getLogsByUserName(String userName);

    List<AuditLogResponseDto> getLogsByStatus(AuditStatus status);

    List<AuditLogResponseDto> getLogsByStatusOrderByTimestampDesc(AuditStatus status);

    List<AuditLogResponseDto> getFailedAndWarningLogs();

    List<AuditLogResponseDto> getLogsByActionContaining(String action);

    List<AuditLogResponseDto> getLogsByActionAndStatus(String action, AuditStatus status);

    List<AuditLogResponseDto> getLogsBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLogResponseDto> getLogsAfter(LocalDateTime timestamp);

    List<AuditLogResponseDto> getLogsBefore(LocalDateTime timestamp);

    List<AuditLogResponseDto> getTodayLogs(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<AuditLogResponseDto> getLogsFromLastDays(int days);

    List<AuditLogResponseDto> getLogsWithDurationGreaterThan(Long duration);

    List<AuditLogResponseDto> getLogsWithDurationGreaterThanOrderByDurationDesc(Long duration);

    List<AuditLogResponseDto> searchAuditLogs(Long userId, AuditStatus status, String action,
                                              LocalDateTime startDate, LocalDateTime endDate);

    long countLogsByStatus(AuditStatus status);

    long countLogsByUserId(Long userId);

    long countTodayLogs();

    Map<String, Map<String, Long>> getLogsCountByDateAndStatus(int lastDays);

    Long getAverageSuccessDuration();

    Long getAverageFailureDuration();

    void deleteLogsOlderThan(LocalDateTime cutoffDate);

    void archiveOldLogs(int daysToKeep);

    boolean existsLogsByUserId(Long userId);

    boolean existsFailedLogsByUserId(Long userId);
}