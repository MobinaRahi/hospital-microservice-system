package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.service.EncounterService;
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

@RestController
@RequestMapping("/api/v1/encounters")
@RequiredArgsConstructor
@Tag(name = "Encounter Management", description = "Encounter (Visit) CRUD and management APIs")
public class EncounterApi {

    private final EncounterService encounterService;

    @PostMapping
    @Operation(summary = "Create a new encounter (visit)")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> createEncounter(@Valid @RequestBody EncounterCreateDto createDto) {
        EncounterResponseDto created = encounterService.createEncounter(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Encounter created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an encounter by ID")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> updateEncounter(@PathVariable Long id, @Valid @RequestBody EncounterUpdateDto updateDto) {
        EncounterResponseDto updated = encounterService.updateEncounter(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Encounter updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete an encounter")
    public ResponseEntity<ApiResponse<Void>> completeEncounter(@PathVariable Long id) {
        encounterService.completeEncounter(id);
        return ResponseEntity.ok(ApiResponse.success("Encounter completed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an encounter")
    public ResponseEntity<ApiResponse<Void>> cancelEncounter(@PathVariable Long id) {
        encounterService.cancelEncounter(id);
        return ResponseEntity.ok(ApiResponse.success("Encounter cancelled successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get encounter by ID")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> getEncounterById(@PathVariable Long id) {
        EncounterResponseDto encounter = encounterService.getEncounterById(id);
        return ResponseEntity.ok(ApiResponse.success(encounter, "Encounter retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get encounters by patient ID")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByPatientId(@PathVariable Long patientId) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get encounters by doctor ID")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByDoctorId(@PathVariable Long doctorId) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get encounters by status")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByStatus(@PathVariable EncounterStatus status) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's encounters")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getTodayEncounters() {
        List<EncounterResponseDto> encounters = encounterService.getTodayEncounters();
        return ResponseEntity.ok(ApiResponse.success(encounters, "Today's encounters retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get encounters by date range")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count encounters by patient ID")
    public ResponseEntity<ApiResponse<Long>> countEncountersByPatientId(@PathVariable Long patientId) {
        Long count = encounterService.countEncountersByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Encounters count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/doctor/{doctorId}")
    @Operation(summary = "Count encounters by doctor ID")
    public ResponseEntity<ApiResponse<Long>> countEncountersByDoctorId(@PathVariable Long doctorId) {
        Long count = encounterService.countEncountersByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(count, "Encounters count retrieved successfully", HttpStatus.OK.value()));
    }
}
