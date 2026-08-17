package hospital.adminservice.service.impl;

import hospital.adminservice.dto.systemlog.SystemLogCreateDto;
import hospital.adminservice.dto.systemlog.SystemLogResponseDto;
import hospital.adminservice.mapper.SystemLogMapper;
import hospital.adminservice.model.SystemLog;
import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import hospital.adminservice.repository.SystemLogRepository;
import hospital.adminservice.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link SystemLogService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;
    private final SystemLogMapper systemLogMapper;

    @Override
    public SystemLogResponseDto createLog(SystemLogCreateDto dto) {
        log.info("Creating system log: {} - {}", dto.getLevel(), dto.getTitle());

        SystemLog logEntry = systemLogMapper.toEntity(dto);
        SystemLog saved = systemLogRepository.save(logEntry);
        log.info("Log created with id: {}", saved.getId());

        return systemLogMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemLogResponseDto getLogById(Long id) {
        log.debug("Fetching log by id: {}", id);

        SystemLog logEntry = systemLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Log with id " + id + " not found"));

        return systemLogMapper.toResponseDto(logEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getAllLogs() {
        log.debug("Fetching all logs");

        List<SystemLog> logs = systemLogRepository.findAllNotDeleted();
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByLevel(LogLevel level) {
        log.debug("Fetching logs by level: {}", level);

        List<SystemLog> logs = systemLogRepository.findByLevel(level);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByCategory(LogCategory category) {
        log.debug("Fetching logs by category: {}", category);

        List<SystemLog> logs = systemLogRepository.findByCategory(category);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByLevelAndCategory(LogLevel level, LogCategory category) {
        log.debug("Fetching logs by level {} and category {}", level, category);

        List<SystemLog> logs = systemLogRepository.findByLevelAndCategory(level, category);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByTenant(Long tenantId) {
        log.debug("Fetching logs for tenant: {}", tenantId);

        List<SystemLog> logs = systemLogRepository.findByRelatedTenantId(tenantId);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByUser(Long userId) {
        log.debug("Fetching logs for user: {}", userId);

        List<SystemLog> logs = systemLogRepository.findByUserId(userId);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching logs between {} and {}", start, end);

        List<SystemLog> logs = systemLogRepository.findByCreatedAtBetween(start, end);
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemLogResponseDto> getSevereLogs() {
        log.debug("Fetching severe logs (ERROR/CRITICAL)");

        List<SystemLog> logs = systemLogRepository.findSevereLogs();
        return systemLogMapper.toResponseDtoList(logs);
    }

    @Override
    public void deleteLog(Long id) {
        log.info("Soft-deleting log id: {}", id);

        SystemLog logEntry = systemLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Log with id " + id + " not found"));

        logEntry.softDelete(null);
        systemLogRepository.save(logEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByLevel(LogLevel level) {
        return systemLogRepository.countByLevel(level);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategory(LogCategory category) {
        return systemLogRepository.countByCategory(category);
    }
}
