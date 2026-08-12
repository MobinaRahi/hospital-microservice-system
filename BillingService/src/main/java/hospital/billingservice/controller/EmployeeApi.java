package hospital.billingservice.controller;

import hospital.billingservice.dto.employee.EmployeeCreateDto;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.dto.employee.EmployeeUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.model.enums.EmployeePosition;
import hospital.billingservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Employee management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>HR/Admin: Full CRUD access</li>
 *   <li>Accountant: Read access for payroll</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Employee CRUD and management APIs")
public class EmployeeApi {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(@Valid @RequestBody EmployeeCreateDto createDto) {
        EmployeeResponseDto created = employeeService.createEmployee(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Employee created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateDto updateDto) {
        EmployeeResponseDto updated = employeeService.updateEmployee(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Employee updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle employee active status")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> toggleActive(@PathVariable Long id) {
        EmployeeResponseDto toggled = employeeService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Employee status toggled successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponseDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(employee, "Employee retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/code/{employeeCode}")
    @Operation(summary = "Get employee by employee code")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeByCode(@PathVariable String employeeCode) {
        EmployeeResponseDto employee = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(ApiResponse.success(employee, "Employee retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get employee by user ID (from AuthService)")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeByUserId(@PathVariable Long userId) {
        EmployeeResponseDto employee = employeeService.getEmployeeByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(employee, "Employee retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getAllActiveEmployees() {
        List<EmployeeResponseDto> employees = employeeService.getAllActiveEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees, "Active employees retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getAllEmployees() {
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees, "Employees retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/position/{position}")
    @Operation(summary = "Get employees by position")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getEmployeesByPosition(@PathVariable EmployeePosition position) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(employees, "Employees retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get active employees by department")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getEmployeesByDepartment(@PathVariable String department) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success(employees, "Employees retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/position/{position}/active")
    @Operation(summary = "Get active employees by position")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getActiveEmployeesByPosition(@PathVariable EmployeePosition position) {
        List<EmployeeResponseDto> employees = employeeService.getActiveEmployeesByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(employees, "Active employees retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/active")
    @Operation(summary = "Count active employees")
    public ResponseEntity<ApiResponse<Long>> countActiveEmployees() {
        long count = employeeService.countActiveEmployees();
        return ResponseEntity.ok(ApiResponse.success(count, "Active employee count retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete (deactivate) an employee")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", HttpStatus.OK.value()));
    }
}
