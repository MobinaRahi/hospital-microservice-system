package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.prescription.PrescriptionCreateDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.model.enums.PrescriptionStatus;
import hospital.clinicalservice.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Prescription management.
 * Handles prescription lifecycle and status changes.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription Management", description = "Prescription CRUD and management APIs")
public class PrescriptionApi {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @Operation(summary = "Create a new prescription")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> createPrescription(@Valid @RequestBody PrescriptionCreateDto createDto) {
        PrescriptionResponseDto created = prescriptionService.createPrescription(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Prescription created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prescription by ID")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> updatePrescription(@PathVariable Long id, @Valid @RequestBody PrescriptionUpdateDto updateDto) {
        PrescriptionResponseDto updated = prescriptionService.updatePrescription(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Prescription updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a prescription")
    public ResponseEntity<ApiResponse<Void>> cancelPrescription(@PathVariable Long id) {
        prescriptionService.cancelPrescription(id);
        return ResponseEntity.ok(ApiResponse.success("Prescription cancelled successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete a prescription")
    public ResponseEntity<ApiResponse<Void>> completePrescription(@PathVariable Long id) {
        prescriptionService.completePrescription(id);
        return ResponseEntity.ok(ApiResponse.success("Prescription completed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> getPrescriptionById(@PathVariable Long id) {
        PrescriptionResponseDto prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(ApiResponse.success(prescription, "Prescription retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/encounter/{encounterId}")
    @Operation(summary = "Get prescriptions by encounter ID")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getPrescriptionsByEncounterId(@PathVariable Long encounterId) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getPrescriptionsByEncounterId(encounterId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get prescriptions by patient ID")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/active")
    @Operation(summary = "Get active prescriptions by patient ID")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getActivePrescriptionsByPatientId(@PathVariable Long patientId) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getActivePrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Active prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get prescriptions by doctor ID")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getPrescriptionsByDoctorId(@PathVariable Long doctorId) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get prescriptions by status")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getPrescriptionsByStatus(@PathVariable PrescriptionStatus status) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getPrescriptionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getExpiredPrescriptions() {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getExpiredPrescriptions();
        return ResponseEntity.ok(ApiResponse.success(prescriptions, "Expired prescriptions retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count prescriptions by patient ID")
    public ResponseEntity<ApiResponse<Long>> countPrescriptionsByPatientId(@PathVariable Long patientId) {
        Long count = prescriptionService.countPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Prescriptions count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/doctor/{doctorId}")
    @Operation(summary = "Count prescriptions by doctor ID")
    public ResponseEntity<ApiResponse<Long>> countPrescriptionsByDoctorId(@PathVariable Long doctorId) {
        Long count = prescriptionService.countPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(count, "Prescriptions count retrieved successfully", HttpStatus.OK.value()));
    }
}
