package hospital.adminservice.repository;

import hospital.adminservice.model.SystemLog;
import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for SystemLog entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface SystemLogRepository extends BaseEntityRepository<SystemLog, Long> {

    List<SystemLog> findByLevel(LogLevel level);

    List<SystemLog> findByCategory(LogCategory category);

    List<SystemLog> findByLevelAndCategory(LogLevel level, LogCategory category);

    List<SystemLog> findByRelatedTenantId(Long relatedTenantId);

    List<SystemLog> findByUserId(Long userId);

    @Query("SELECT l FROM SystemLog l WHERE l.createdAt BETWEEN :start AND :end AND l.deleted = false ORDER BY l.createdAt DESC")
    List<SystemLog> findByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT l FROM SystemLog l WHERE l.level IN ('ERROR', 'CRITICAL') AND l.deleted = false ORDER BY l.createdAt DESC")
    List<SystemLog> findSevereLogs();

    long countByLevel(LogLevel level);

    long countByCategory(LogCategory category);

    long countByRelatedTenantId(Long relatedTenantId);
}
