package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.model.enums.AuditStatus;
import hospital.coreservice.service.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit log search and statistics APIs")
public class AuditLogApi {
    private final AuditLogService auditLogService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> byUser(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByUserId(userId), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/user/{userId}/desc")
    public ResponseEntity<ApiResponse> byUserDesc(@PathVariable Long userId) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByUserIdOrderByTimestampDesc(userId), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse> byUserName(@RequestParam String userName) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByUserName(userName), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse> byStatus(@RequestParam AuditStatus status) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByStatus(status), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/failed-warning")
    public ResponseEntity<ApiResponse> failedAndWarning() { return ResponseEntity.ok(ApiResponse.success(auditLogService.getFailedAndWarningLogs(), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/by-action")
    public ResponseEntity<ApiResponse> byAction(@RequestParam String action) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByActionContaining(action), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) AuditStatus status,
                                              @RequestParam(required = false) String action,
                                              @RequestParam(required = false) LocalDateTime startDate,
                                              @RequestParam(required = false) LocalDateTime endDate) {
        return ResponseEntity.ok(ApiResponse.success(auditLogService.searchAuditLogs(userId, status, action, startDate, endDate), "Logs found", HttpStatus.OK.value()));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse> today() {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(ApiResponse.success(auditLogService.getTodayLogs(today.atStartOfDay(), today.plusDays(1).atStartOfDay()), "Today logs loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/last-days")
    public ResponseEntity<ApiResponse> lastDays(@RequestParam(defaultValue = "7") int days) { return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsFromLastDays(days), "Logs loaded", HttpStatus.OK.value())); }

    @GetMapping("/count/today")
    public ResponseEntity<ApiResponse> countToday() { return ResponseEntity.ok(ApiResponse.success(auditLogService.countTodayLogs(), "Today logs count", HttpStatus.OK.value())); }

    @GetMapping("/count/by-status")
    public ResponseEntity<ApiResponse> countByStatus(@RequestParam AuditStatus status) { return ResponseEntity.ok(ApiResponse.success(auditLogService.countLogsByStatus(status), "Logs count", HttpStatus.OK.value())); }

    @DeleteMapping("/older-than")
    public ResponseEntity<ApiResponse> deleteOlderThan(@RequestParam LocalDateTime cutoffDate) {
        auditLogService.deleteLogsOlderThan(cutoffDate);
        return ResponseEntity.ok(ApiResponse.success("Old logs deleted", HttpStatus.OK.value()));
    }
}
