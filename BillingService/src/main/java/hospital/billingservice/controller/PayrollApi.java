package hospital.billingservice.controller;

import hospital.billingservice.dto.payroll.PayrollCreateDto;
import hospital.billingservice.dto.payroll.PayrollResponseDto;
import hospital.billingservice.dto.payroll.PayrollUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.model.enums.PayrollStatus;
import hospital.billingservice.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for Payroll management.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → PROCESSED → PAID
 *                    ↓
 *                CANCELLED
 * </pre>
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>HR/Accountant: Full access</li>
 *   <li>Employee: Read own payroll</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/payrolls")
@RequiredArgsConstructor
@Tag(name = "Payroll Management", description = "Payroll CRUD and status management APIs")
public class PayrollApi {

    private final PayrollService payrollService;

    @PostMapping
    @Operation(summary = "Create a new payroll record")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> createPayroll(@Valid @RequestBody PayrollCreateDto createDto) {
        PayrollResponseDto created = payrollService.createPayroll(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Payroll created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payroll record by ID")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> updatePayroll(@PathVariable Long id, @Valid @RequestBody PayrollUpdateDto updateDto) {
        PayrollResponseDto updated = payrollService.updatePayroll(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payroll updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/process")
    @Operation(summary = "Process a payroll record (PENDING → PROCESSED)")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> processPayroll(@PathVariable Long id) {
        PayrollResponseDto processed = payrollService.processPayroll(id);
        return ResponseEntity.ok(ApiResponse.success(processed, "Payroll processed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/mark-paid")
    @Operation(summary = "Mark payroll as paid (PROCESSED → PAID)")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> markAsPaid(@PathVariable Long id) {
        PayrollResponseDto paid = payrollService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(paid, "Payroll marked as paid", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a payroll record")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> cancelPayroll(@PathVariable Long id) {
        PayrollResponseDto cancelled = payrollService.cancelPayroll(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Payroll cancelled successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payroll record by ID")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> getPayrollById(@PathVariable Long id) {
        PayrollResponseDto payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(ApiResponse.success(payroll, "Payroll retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all payroll records for an employee")
    public ResponseEntity<ApiResponse<List<PayrollResponseDto>>> getPayrollsByEmployee(@PathVariable Long employeeId) {
        List<PayrollResponseDto> payrolls = payrollService.getPayrollsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success(payrolls, "Payrolls retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/employee/{employeeId}/period")
    @Operation(summary = "Get payroll for an employee in a specific month/year")
    public ResponseEntity<ApiResponse<PayrollResponseDto>> getPayrollByEmployeeAndPeriod(
            @PathVariable Long employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        PayrollResponseDto payroll = payrollService.getPayrollByEmployeeAndPeriod(employeeId, month, year);
        return ResponseEntity.ok(ApiResponse.success(payroll, "Payroll retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/period")
    @Operation(summary = "Get all payroll records for a specific month/year")
    public ResponseEntity<ApiResponse<List<PayrollResponseDto>>> getPayrollsByPeriod(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayrollResponseDto> payrolls = payrollService.getPayrollsByPeriod(month, year);
        return ResponseEntity.ok(ApiResponse.success(payrolls, "Payrolls retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payroll records by status")
    public ResponseEntity<ApiResponse<List<PayrollResponseDto>>> getPayrollsByStatus(@PathVariable PayrollStatus status) {
        List<PayrollResponseDto> payrolls = payrollService.getPayrollsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(payrolls, "Payrolls retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending payroll records")
    public ResponseEntity<ApiResponse<List<PayrollResponseDto>>> getPendingPayrolls() {
        List<PayrollResponseDto> payrolls = payrollService.getPendingPayrolls();
        return ResponseEntity.ok(ApiResponse.success(payrolls, "Pending payrolls retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/calculate-net-salary")
    @Operation(summary = "Calculate net salary for a payroll record")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateNetSalary(@PathVariable Long id) {
        BigDecimal netSalary = payrollService.calculateNetSalary(id);
        return ResponseEntity.ok(ApiResponse.success(netSalary, "Net salary calculated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a payroll record")
    public ResponseEntity<ApiResponse<Void>> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok(ApiResponse.success("Payroll deleted successfully", HttpStatus.OK.value()));
    }
}
