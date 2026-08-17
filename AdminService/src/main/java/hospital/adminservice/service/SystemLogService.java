package hospital.adminservice.service;

import hospital.adminservice.dto.systemlog.SystemLogCreateDto;
import hospital.adminservice.dto.systemlog.SystemLogResponseDto;
import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for SystemLog management.
 *
 * @author MobinaRahi
 */
public interface SystemLogService {

    SystemLogResponseDto createLog(SystemLogCreateDto dto);

    SystemLogResponseDto getLogById(Long id);

    List<SystemLogResponseDto> getAllLogs();

    List<SystemLogResponseDto> getLogsByLevel(LogLevel level);

    List<SystemLogResponseDto> getLogsByCategory(LogCategory category);

    List<SystemLogResponseDto> getLogsByLevelAndCategory(LogLevel level, LogCategory category);

    List<SystemLogResponseDto> getLogsByTenant(Long tenantId);

    List<SystemLogResponseDto> getLogsByUser(Long userId);

    List<SystemLogResponseDto> getLogsByDateRange(LocalDateTime start, LocalDateTime end);

    List<SystemLogResponseDto> getSevereLogs();

    void deleteLog(Long id);

    long countByLevel(LogLevel level);

    long countByCategory(LogCategory category);
}
