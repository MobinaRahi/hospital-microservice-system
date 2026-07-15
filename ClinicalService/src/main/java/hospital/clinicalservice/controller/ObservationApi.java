package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.observation.ObservationCreateDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.observation.ObservationUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.service.ObservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/observations")
@RequiredArgsConstructor
@Tag(name = "Observation Management", description = "Observation (Vital Signs) CRUD and management APIs (LOINC)")
public class ObservationApi {

    private final ObservationService observationService;

    @PostMapping
    @Operation(summary = "Create a new observation (vital sign)")
    public ResponseEntity<ApiResponse<ObservationResponseDto>> createObservation(@Valid @RequestBody ObservationCreateDto createDto) {
        ObservationResponseDto created = observationService.createObservation(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Observation created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an observation by ID")
    public ResponseEntity<ApiResponse<ObservationResponseDto>> updateObservation(@PathVariable Long id, @Valid @RequestBody ObservationUpdateDto updateDto) {
        ObservationResponseDto updated = observationService.updateObservation(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Observation updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an observation by ID")
    public ResponseEntity<ApiResponse<Void>> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.ok(ApiResponse.success("Observation deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get observation by ID")
    public ResponseEntity<ApiResponse<ObservationResponseDto>> getObservationById(@PathVariable Long id) {
        ObservationResponseDto observation = observationService.getObservationById(id);
        return ResponseEntity.ok(ApiResponse.success(observation, "Observation retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/encounter/{encounterId}")
    @Operation(summary = "Get observations by encounter ID")
    public ResponseEntity<ApiResponse<List<ObservationResponseDto>>> getObservationsByEncounterId(@PathVariable Long encounterId) {
        List<ObservationResponseDto> observations = observationService.getObservationsByEncounterId(encounterId);
        return ResponseEntity.ok(ApiResponse.success(observations, "Observations retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get observations by patient ID")
    public ResponseEntity<ApiResponse<List<ObservationResponseDto>>> getObservationsByPatientId(@PathVariable Long patientId) {
        List<ObservationResponseDto> observations = observationService.getObservationsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(observations, "Observations retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/loinc/{loincCode}")
    @Operation(summary = "Get observations by patient ID and LOINC code")
    public ResponseEntity<ApiResponse<List<ObservationResponseDto>>> getObservationsByPatientIdAndLoincCode(
            @PathVariable Long patientId, @PathVariable String loincCode) {
        List<ObservationResponseDto> observations = observationService.getObservationsByPatientIdAndLoincCode(patientId, loincCode);
        return ResponseEntity.ok(ApiResponse.success(observations, "Observations retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/abnormal")
    @Operation(summary = "Get abnormal observations by patient ID")
    public ResponseEntity<ApiResponse<List<ObservationResponseDto>>> getAbnormalObservationsByPatientId(@PathVariable Long patientId) {
        List<ObservationResponseDto> observations = observationService.getAbnormalObservationsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(observations, "Abnormal observations retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/latest/{loincCode}")
    @Operation(summary = "Get latest observation by patient ID and LOINC code")
    public ResponseEntity<ApiResponse<ObservationResponseDto>> getLatestObservationByPatientAndLoinc(
            @PathVariable Long patientId, @PathVariable String loincCode) {
        ObservationResponseDto observation = observationService.getLatestObservationByPatientAndLoinc(patientId, loincCode);
        return ResponseEntity.ok(ApiResponse.success(observation, "Latest observation retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count observations by patient ID")
    public ResponseEntity<ApiResponse<Long>> countObservationsByPatientId(@PathVariable Long patientId) {
        Long count = observationService.countObservationsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Observations count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}/abnormal")
    @Operation(summary = "Count abnormal observations by patient ID")
    public ResponseEntity<ApiResponse<Long>> countAbnormalObservationsByPatientId(@PathVariable Long patientId) {
        Long count = observationService.countAbnormalObservationsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Abnormal observations count retrieved successfully", HttpStatus.OK.value()));
    }
}
