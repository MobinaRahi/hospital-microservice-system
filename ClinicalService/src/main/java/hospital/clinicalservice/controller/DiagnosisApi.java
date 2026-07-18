package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.diagnosis.DiagnosisCreateDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Diagnosis management.
 * Uses ICD-10 standard codes for medical diagnoses.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/diagnoses")
@RequiredArgsConstructor
@Tag(name = "Diagnosis Management", description = "Diagnosis CRUD and management APIs (ICD-10)")
public class DiagnosisApi {

    // ==================== Dependencies ====================

    private final DiagnosisService diagnosisService;

    // ==================== Create ====================

    @PostMapping
    @Operation(summary = "Create a new diagnosis")
    public ResponseEntity<ApiResponse<DiagnosisResponseDto>> createDiagnosis(@Valid @RequestBody DiagnosisCreateDto createDto) {
        DiagnosisResponseDto created = diagnosisService.createDiagnosis(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Diagnosis created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a diagnosis by ID")
    public ResponseEntity<ApiResponse<DiagnosisResponseDto>> updateDiagnosis(@PathVariable Long id, @Valid @RequestBody DiagnosisUpdateDto updateDto) {
        DiagnosisResponseDto updated = diagnosisService.updateDiagnosis(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Diagnosis updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a diagnosis by ID")
    public ResponseEntity<ApiResponse<Void>> deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        return ResponseEntity.ok(ApiResponse.success("Diagnosis deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get diagnosis by ID")
    public ResponseEntity<ApiResponse<DiagnosisResponseDto>> getDiagnosisById(@PathVariable Long id) {
        DiagnosisResponseDto diagnosis = diagnosisService.getDiagnosisById(id);
        return ResponseEntity.ok(ApiResponse.success(diagnosis, "Diagnosis retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/encounter/{encounterId}")
    @Operation(summary = "Get diagnoses by encounter ID")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDto>>> getDiagnosesByEncounterId(@PathVariable Long encounterId) {
        List<DiagnosisResponseDto> diagnoses = diagnosisService.getDiagnosesByEncounterId(encounterId);
        return ResponseEntity.ok(ApiResponse.success(diagnoses, "Diagnoses retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/encounter/{encounterId}/primary")
    @Operation(summary = "Get primary diagnoses by encounter ID")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDto>>> getPrimaryDiagnosesByEncounterId(@PathVariable Long encounterId) {
        List<DiagnosisResponseDto> diagnoses = diagnosisService.getPrimaryDiagnosesByEncounterId(encounterId);
        return ResponseEntity.ok(ApiResponse.success(diagnoses, "Primary diagnoses retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get diagnoses by patient ID")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDto>>> getDiagnosesByPatientId(@PathVariable Long patientId) {
        List<DiagnosisResponseDto> diagnoses = diagnosisService.getDiagnosesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(diagnoses, "Diagnoses retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/icd/{icd10Code}")
    @Operation(summary = "Get diagnoses by ICD-10 code")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDto>>> getDiagnosesByIcd10Code(@PathVariable String icd10Code) {
        List<DiagnosisResponseDto> diagnoses = diagnosisService.getDiagnosesByIcd10Code(icd10Code);
        return ResponseEntity.ok(ApiResponse.success(diagnoses, "Diagnoses retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search diagnoses by query")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDto>>> searchDiagnoses(@RequestParam String query) {
        List<DiagnosisResponseDto> diagnoses = diagnosisService.searchDiagnoses(query);
        return ResponseEntity.ok(ApiResponse.success(diagnoses, "Diagnoses found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/encounter/{encounterId}")
    @Operation(summary = "Count diagnoses by encounter ID")
    public ResponseEntity<ApiResponse<Long>> countDiagnosesByEncounterId(@PathVariable Long encounterId) {
        Long count = diagnosisService.countDiagnosesByEncounterId(encounterId);
        return ResponseEntity.ok(ApiResponse.success(count, "Diagnoses count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count diagnoses by patient ID")
    public ResponseEntity<ApiResponse<Long>> countDiagnosesByPatientId(@PathVariable Long patientId) {
        Long count = diagnosisService.countDiagnosesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Diagnoses count retrieved successfully", HttpStatus.OK.value()));
    }
}
