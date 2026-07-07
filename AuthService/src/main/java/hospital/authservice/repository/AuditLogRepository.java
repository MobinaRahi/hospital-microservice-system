package hospital.authservice.repository;

import hospital.authservice.model.AuditLog;
import hospital.authservice.model.enums.AuditStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends BaseEntityRepository<AuditLog, Long> {

    // ========== Find by User ==========
    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);

    List<AuditLog> findByUserName(String userName);

    // ========== Find by Status ==========
    List<AuditLog> findByStatus(AuditStatus status);

    List<AuditLog> findByStatusOrderByTimestampDesc(AuditStatus status);

    @Query("SELECT a FROM auditLogEntity a WHERE a.status = 'FAILURE' OR a.status = 'WARNING' ORDER BY a.timestamp DESC")
    List<AuditLog> findFailedAndWarningLogs();

    // ========== Find by Action ==========
    List<AuditLog> findByActionContainingIgnoreCase(String action);

    List<AuditLog> findByActionContainingIgnoreCaseAndStatus(String action, AuditStatus status);

    // ========== Date Range ==========
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByTimestampAfter(LocalDateTime timestamp);

    List<AuditLog> findByTimestampBefore(LocalDateTime timestamp);

    @Query("SELECT a FROM auditLogEntity a WHERE a.timestamp >= :startOfDay AND a.timestamp < :endOfDay")
    List<AuditLog> findTodayLogs(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT a FROM auditLogEntity a WHERE a.timestamp >= :fromDate ORDER BY a.timestamp DESC")
    List<AuditLog> findLogsFromLastDays(@Param("fromDate") LocalDateTime fromDate);

    // ========== Find by Duration ==========
    List<AuditLog> findByDurationGreaterThan(Long duration);

    List<AuditLog> findByDurationGreaterThanOrderByDurationDesc(Long duration);

    // ========== Advanced Search ==========
    @Query("""
                SELECT a FROM auditLogEntity a
                WHERE (:userId IS NULL OR a.user.id = :userId)
                  AND (:status IS NULL OR a.status = :status)
                  AND (:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%')))
                  AND (:startDate IS NULL OR a.timestamp >= :startDate)
                  AND (:endDate IS NULL OR a.timestamp <= :endDate)
                ORDER BY a.timestamp DESC
            """)
    List<AuditLog> searchAuditLogs(
            @Param("userId") Long userId,
            @Param("status") AuditStatus status,
            @Param("action") String action,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // ========== Statistics ==========
    long countByStatus(AuditStatus status);

    long countByUserId(Long userId);

    @Query("SELECT COUNT(a) FROM auditLogEntity a WHERE CAST(a.createdAt AS date) = CURRENT_DATE")
    long countTodayLogs();

    @Query("""
                SELECT DATE(a.timestamp) as date,
                       a.status as status,
                       COUNT(a) as count
                FROM auditLogEntity a
                WHERE a.timestamp >= :fromDate
                GROUP BY DATE(a.timestamp), a.status
                ORDER BY DATE(a.timestamp) DESC
            """)
    List<Object[]> countLogsByDateAndStatus(@Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT AVG(a.duration) FROM auditLogEntity a WHERE a.status = 'SUCCESS' AND a.duration IS NOT NULL")
    Long getAverageSuccessDuration();

    @Query("SELECT AVG(a.duration) FROM auditLogEntity a WHERE a.status = 'FAILURE' AND a.duration IS NOT NULL")
    Long getAverageFailureDuration();

    // ========== Cleanup ==========
    @Modifying
    @Transactional
    @Query("DELETE FROM auditLogEntity a WHERE a.timestamp < :cutoffDate")
    long deleteLogsOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    // ========== Existence Checks ==========
    boolean existsByUserId(Long userId);

    @Query("SELECT COUNT(a) > 0 FROM auditLogEntity a WHERE a.user.id = :userId AND (a.status = 'FAILURE' OR a.status = 'WARNING')")
    boolean existsFailedLogsByUserId(@Param("userId") Long userId);
}
