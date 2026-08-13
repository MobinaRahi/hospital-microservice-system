package hospital.adminservice.controller;

import hospital.adminservice.dto.report.ReportCreateDto;
import hospital.adminservice.dto.report.ReportResponseDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.model.enums.ReportStatus;
import hospital.adminservice.model.enums.ReportType;
import hospital.adminservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Report management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin: Full access + all reports</li>
 *   <li>User: Create and read own reports</li>
 * </ul>
 *
 * <p><strong>Report Workflow:</strong></p>
 * <pre>
 * CREATE → PENDING → PROCESSING → COMPLETED
 *                                  → FAILED
 * </pre>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "Report generation and management APIs")
public class ReportApi {

    private final ReportService reportService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ══════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new report generation request")
    public ResponseEntity<ApiResponse<ReportResponseDto>> createReport(@Valid @RequestBody ReportCreateDto createDto) {
        ReportResponseDto created = reportService.createReport(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Report created successfully", HttpStatus.CREATED.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID")
    public ResponseEntity<ApiResponse<ReportResponseDto>> getReportById(@PathVariable Long id) {
        ReportResponseDto report = reportService.getReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get reports by type")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByType(@PathVariable ReportType type) {
        List<ReportResponseDto> reports = reportService.getReportsByType(type);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByStatus(@PathVariable ReportStatus status) {
        List<ReportResponseDto> reports = reportService.getReportsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reports by user")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByUser(@PathVariable Long userId) {
        List<ReportResponseDto> reports = reportService.getReportsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/range")
    @Operation(summary = "Get reports in a date range")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ReportResponseDto> reports = reportService.getReportsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending reports")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getPendingReports() {
        List<ReportResponseDto> reports = reportService.getPendingReports();
        return ResponseEntity.ok(ApiResponse.success(reports, "Pending reports retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired reports for cleanup")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getExpiredReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) {
        List<ReportResponseDto> reports = reportService.getExpiredReports(beforeDate);
        return ResponseEntity.ok(ApiResponse.success(reports, "Expired reports retrieved successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Status Management
    // ═══════════════════════════════════════════════════════════════════

    @PostMapping("/{id}/processing")
    @Operation(summary = "Mark report as processing")
    public ResponseEntity<ApiResponse<ReportResponseDto>> markAsProcessing(@PathVariable Long id) {
        ReportResponseDto updated = reportService.markAsProcessing(id);
        return ResponseEntity.ok(ApiResponse.success(updated, "Report marked as processing", HttpStatus.OK.value()));
    }

    @PostMapping("/{id}/completed")
    @Operation(summary = "Mark report as completed with file URL")
    public ResponseEntity<ApiResponse<ReportResponseDto>> markAsCompleted(
            @PathVariable Long id,
            @RequestParam String fileUrl) {
        ReportResponseDto updated = reportService.markAsCompleted(id, fileUrl);
        return ResponseEntity.ok(ApiResponse.success(updated, "Report marked as completed", HttpStatus.OK.value()));
    }

    @PostMapping("/{id}/failed")
    @Operation(summary = "Mark report as failed")
    public ResponseEntity<ApiResponse<ReportResponseDto>> markAsFailed(@PathVariable Long id) {
        ReportResponseDto updated = reportService.markAsFailed(id);
        return ResponseEntity.ok(ApiResponse.success(updated, "Report marked as failed", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a report")
    public ResponseEntity<ApiResponse<Void>> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok(ApiResponse.success("Report deleted successfully", HttpStatus.OK.value()));
    }
}
