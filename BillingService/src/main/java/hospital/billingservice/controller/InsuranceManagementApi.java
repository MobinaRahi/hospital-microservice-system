package hospital.billingservice.controller;

import hospital.billingservice.dto.insurancemanagement.InsuranceManagementCreateDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.service.InsuranceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for InsuranceManagement.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin: Full CRUD access</li>
 *   <li>Accountant: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/insurances")
@RequiredArgsConstructor
@Tag(name = "Insurance Management", description = "Insurance plan CRUD and management APIs")
public class InsuranceManagementApi {

    private final InsuranceManagementService insuranceService;

    @PostMapping
    @Operation(summary = "Create a new insurance plan")
    public ResponseEntity<ApiResponse<InsuranceManagementResponseDto>> createInsurance(@Valid @RequestBody InsuranceManagementCreateDto createDto) {
        InsuranceManagementResponseDto created = insuranceService.createInsurance(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Insurance plan created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an insurance plan by ID")
    public ResponseEntity<ApiResponse<InsuranceManagementResponseDto>> updateInsurance(@PathVariable Long id, @Valid @RequestBody InsuranceManagementUpdateDto updateDto) {
        InsuranceManagementResponseDto updated = insuranceService.updateInsurance(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Insurance plan updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle insurance plan active status")
    public ResponseEntity<ApiResponse<InsuranceManagementResponseDto>> toggleActive(@PathVariable Long id) {
        InsuranceManagementResponseDto toggled = insuranceService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Insurance plan status toggled successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get insurance plan by ID")
    public ResponseEntity<ApiResponse<InsuranceManagementResponseDto>> getInsuranceById(@PathVariable Long id) {
        InsuranceManagementResponseDto insurance = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(ApiResponse.success(insurance, "Insurance plan retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get insurance plan by code")
    public ResponseEntity<ApiResponse<InsuranceManagementResponseDto>> getInsuranceByCode(@PathVariable String code) {
        InsuranceManagementResponseDto insurance = insuranceService.getInsuranceByCode(code);
        return ResponseEntity.ok(ApiResponse.success(insurance, "Insurance plan retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active insurance plans")
    public ResponseEntity<ApiResponse<List<InsuranceManagementResponseDto>>> getAllActiveInsurances() {
        List<InsuranceManagementResponseDto> insurances = insuranceService.getAllActiveInsurances();
        return ResponseEntity.ok(ApiResponse.success(insurances, "Active insurance plans retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all insurance plans")
    public ResponseEntity<ApiResponse<List<InsuranceManagementResponseDto>>> getAllInsurances() {
        List<InsuranceManagementResponseDto> insurances = insuranceService.getAllInsurances();
        return ResponseEntity.ok(ApiResponse.success(insurances, "Insurance plans retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search insurance plans by name")
    public ResponseEntity<ApiResponse<List<InsuranceManagementResponseDto>>> searchInsurances(@RequestParam String name) {
        List<InsuranceManagementResponseDto> insurances = insuranceService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success(insurances, "Insurance plans retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an insurance plan")
    public ResponseEntity<ApiResponse<Void>> deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.ok(ApiResponse.success("Insurance plan deleted successfully", HttpStatus.OK.value()));
    }
}
