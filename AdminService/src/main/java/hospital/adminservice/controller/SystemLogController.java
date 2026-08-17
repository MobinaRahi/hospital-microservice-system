package hospital.adminservice.controller;

import hospital.adminservice.dto.systemlog.SystemLogCreateDto;
import hospital.adminservice.dto.systemlog.SystemLogResponseDto;
import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import hospital.adminservice.service.SystemLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for SystemLog management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/super-admin/logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "System Logs", description = "Audit trail and system log management")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @PostMapping
    @Operation(summary = "Create a new system log entry")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Log created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<SystemLogResponseDto> createLog(@Valid @RequestBody SystemLogCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(systemLogService.createLog(dto));
    }

    @GetMapping
    @Operation(summary = "Get all system logs with optional filters")
    public ResponseEntity<List<SystemLogResponseDto>> getAllLogs(
            @RequestParam(required = false) LogLevel level,
            @RequestParam(required = false) LogCategory category,
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long userId) {

        if (level != null && category != null) {
            return ResponseEntity.ok(systemLogService.getLogsByLevelAndCategory(level, category));
        }
        if (level != null) {
            return ResponseEntity.ok(systemLogService.getLogsByLevel(level));
        }
        if (category != null) {
            return ResponseEntity.ok(systemLogService.getLogsByCategory(category));
        }
        if (tenantId != null) {
            return ResponseEntity.ok(systemLogService.getLogsByTenant(tenantId));
        }
        if (userId != null) {
            return ResponseEntity.ok(systemLogService.getLogsByUser(userId));
        }
        return ResponseEntity.ok(systemLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get log by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log found"),
            @ApiResponse(responseCode = "404", description = "Log not found")
    })
    public ResponseEntity<SystemLogResponseDto> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(systemLogService.getLogById(id));
    }

    @GetMapping("/severe")
    @Operation(summary = "Get severe logs (ERROR/CRITICAL)")
    public ResponseEntity<List<SystemLogResponseDto>> getSevereLogs() {
        return ResponseEntity.ok(systemLogService.getSevereLogs());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get logs within a date range")
    public ResponseEntity<List<SystemLogResponseDto>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(systemLogService.getLogsByDateRange(start, end));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a log")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        systemLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get log count statistics")
    public ResponseEntity<Long> countLogs(
            @RequestParam(required = false) LogLevel level,
            @RequestParam(required = false) LogCategory category) {
        if (level != null) {
            return ResponseEntity.ok(systemLogService.countByLevel(level));
        }
        if (category != null) {
            return ResponseEntity.ok(systemLogService.countByCategory(category));
        }
        return ResponseEntity.ok((long) systemLogService.getAllLogs().size());
    }
}
