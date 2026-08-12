package hospital.billingservice.controller;

import hospital.billingservice.dto.patientinsurance.PatientInsuranceCreateDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceResponseDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.service.PatientInsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for PatientInsurance.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin/Receptionist: Full CRUD access</li>
 *   <li>Patient: Read own insurance records</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/patient-insurances")
@RequiredArgsConstructor
@Tag(name = "Patient Insurance Management", description = "Patient insurance CRUD and management APIs")
public class PatientInsuranceApi {

    private final PatientInsuranceService patientInsuranceService;

    @PostMapping
    @Operation(summary = "Create a new patient insurance record")
    public ResponseEntity<ApiResponse<PatientInsuranceResponseDto>> createPatientInsurance(@Valid @RequestBody PatientInsuranceCreateDto createDto) {
        PatientInsuranceResponseDto created = patientInsuranceService.createPatientInsurance(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Patient insurance created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient insurance record by ID")
    public ResponseEntity<ApiResponse<PatientInsuranceResponseDto>> updatePatientInsurance(@PathVariable Long id, @Valid @RequestBody PatientInsuranceUpdateDto updateDto) {
        PatientInsuranceResponseDto updated = patientInsuranceService.updatePatientInsurance(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Patient insurance updated successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient insurance by ID")
    public ResponseEntity<ApiResponse<PatientInsuranceResponseDto>> getPatientInsuranceById(@PathVariable Long id) {
        PatientInsuranceResponseDto patientInsurance = patientInsuranceService.getPatientInsuranceById(id);
        return ResponseEntity.ok(ApiResponse.success(patientInsurance, "Patient insurance retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all insurance records for a patient")
    public ResponseEntity<ApiResponse<List<PatientInsuranceResponseDto>>> getInsurancesByPatient(@PathVariable Long patientId) {
        List<PatientInsuranceResponseDto> insurances = patientInsuranceService.getInsurancesByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success(insurances, "Patient insurances retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/primary")
    @Operation(summary = "Get primary insurance for a patient")
    public ResponseEntity<ApiResponse<PatientInsuranceResponseDto>> getPrimaryInsurance(@PathVariable Long patientId) {
        PatientInsuranceResponseDto insurance = patientInsuranceService.getPrimaryInsurance(patientId);
        return ResponseEntity.ok(ApiResponse.success(insurance, "Primary insurance retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/valid")
    @Operation(summary = "Get valid (non-expired) insurances for a patient")
    public ResponseEntity<ApiResponse<List<PatientInsuranceResponseDto>>> getValidInsurances(@PathVariable Long patientId) {
        List<PatientInsuranceResponseDto> insurances = patientInsuranceService.getValidInsurances(patientId);
        return ResponseEntity.ok(ApiResponse.success(insurances, "Valid insurances retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a patient insurance record")
    public ResponseEntity<ApiResponse<Void>> deletePatientInsurance(@PathVariable Long id) {
        patientInsuranceService.deletePatientInsurance(id);
        return ResponseEntity.ok(ApiResponse.success("Patient insurance deleted successfully", HttpStatus.OK.value()));
    }
}
