package hospital.adminservice.controller;

import hospital.adminservice.dto.employeeshift.EmployeeShiftCreateDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftResponseDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftUpdateDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.service.EmployeeShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for EmployeeShift management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin/HR: Full CRUD access + attendance marking</li>
 *   <li>Employee: Read own shifts</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/employee-shifts")
@RequiredArgsConstructor
@Tag(name = "Employee Shift Management", description = "Employee shift assignment and attendance APIs")
public class EmployeeShiftApi {

    private final EmployeeShiftService employeeShiftService;

    // ══════════════════════════════════════════════════════════════════
    // Create
    // ═════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new employee shift assignment")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> createEmployeeShift(
            @Valid @RequestBody EmployeeShiftCreateDto createDto) {
        EmployeeShiftResponseDto created = employeeShiftService.createEmployeeShift(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Employee shift created successfully", HttpStatus.CREATED.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get employee shift by ID")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> getEmployeeShiftById(@PathVariable Long id) {
        EmployeeShiftResponseDto shift = employeeShiftService.getEmployeeShiftById(id);
        return ResponseEntity.ok(ApiResponse.success(shift, "Employee shift retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/employee/{employeeId}/date/{date}")
    @Operation(summary = "Get employee shift by date")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> getEmployeeShiftByDate(
            @PathVariable Long employeeId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        EmployeeShiftResponseDto shift = employeeShiftService.getEmployeeShiftByDate(employeeId, date);
        return ResponseEntity.ok(ApiResponse.success(shift, "Employee shift retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all shifts for an employee")
    public ResponseEntity<ApiResponse<List<EmployeeShiftResponseDto>>> getShiftsByEmployee(@PathVariable Long employeeId) {
        List<EmployeeShiftResponseDto> shifts = employeeShiftService.getShiftsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success(shifts, "Employee shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/shift/{shiftId}/date/{date}")
    @Operation(summary = "Get all employees on a shift on a specific date")
    public ResponseEntity<ApiResponse<List<EmployeeShiftResponseDto>>> getShiftsByDate(
            @PathVariable Long shiftId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<EmployeeShiftResponseDto> shifts = employeeShiftService.getShiftsByDate(shiftId, date);
        return ResponseEntity.ok(ApiResponse.success(shifts, "Employee shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/range")
    @Operation(summary = "Get employee shifts in a date range")
    public ResponseEntity<ApiResponse<List<EmployeeShiftResponseDto>>> getShiftsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<EmployeeShiftResponseDto> shifts = employeeShiftService.getShiftsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(shifts, "Employee shifts retrieved successfully", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Attendance
    // ══════════════════════════════════════════════════════════════════

    @PostMapping("/{id}/mark-present")
    @Operation(summary = "Mark employee as present and record actual times")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> markPresent(
            @PathVariable Long id,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        EmployeeShiftResponseDto updated = employeeShiftService.markPresent(id, start, end);
        return ResponseEntity.ok(ApiResponse.success(updated, "Employee marked as present", HttpStatus.OK.value()));
    }

    @PostMapping("/{id}/mark-absent")
    @Operation(summary = "Mark employee as absent")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> markAbsent(@PathVariable Long id) {
        EmployeeShiftResponseDto updated = employeeShiftService.markAbsent(id);
        return ResponseEntity.ok(ApiResponse.success(updated, "Employee marked as absent", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═════════════════════════════════════════════════════════════════

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee shift assignment")
    public ResponseEntity<ApiResponse<EmployeeShiftResponseDto>> updateEmployeeShift(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeShiftUpdateDto updateDto) {
        EmployeeShiftResponseDto updated = employeeShiftService.updateEmployeeShift(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Employee shift updated successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an employee shift assignment")
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeShift(@PathVariable Long id) {
        employeeShiftService.deleteEmployeeShift(id);
        return ResponseEntity.ok(ApiResponse.success("Employee shift deleted successfully", HttpStatus.OK.value()));
    }
}
