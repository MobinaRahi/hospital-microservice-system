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

/**
 * REST controller for Encounter management.
 * Provides CRUD operations and status management for patient encounters.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Doctors: Create, update, complete encounters for their patients</li>
 *   <li>Nurses: View encounters, add observations</li>
 *   <li>Admin: View all encounters, statistics</li>
 *   <li>Patients: View their own encounters</li>
 * </ul>
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/encounters")
@RequiredArgsConstructor
@Tag(name = "Encounter Management", description = "Encounter (Visit) CRUD and management APIs")
public class EncounterApi {

    // ==================== Dependencies ====================

    private final EncounterService encounterService;

    // ==================== Create ====================

    /**
     * Creates a new encounter (visit) for a patient.
     * Called when a patient starts a visit with a doctor.
     *
     * <p><strong>Required fields:</strong> patientId, doctorId, type</p>
     * <p><strong>Optional fields:</strong> appointmentId, departmentId, chiefComplaint</p>
     */
    @PostMapping
    @Operation(summary = "Create a new encounter (visit)")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> createEncounter(@Valid @RequestBody EncounterCreateDto createDto) {
        EncounterResponseDto created = encounterService.createEncounter(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Encounter created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing encounter's details.
     * Can update: type, chiefComplaint, doctorNotes
     * Cannot update: patientId, doctorId (these are immutable)
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an encounter by ID")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> updateEncounter(@PathVariable Long id, @Valid @RequestBody EncounterUpdateDto updateDto) {
        EncounterResponseDto updated = encounterService.updateEncounter(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Encounter updated successfully", HttpStatus.OK.value()));
    }

    // ==================== Status Changes ====================

    /**
     * Completes an encounter — marks visit as finished.
     * Called when doctor finishes the visit and all data is recorded.
     * Status changes: IN_PROGRESS → COMPLETED
     */
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete an encounter")
    public ResponseEntity<ApiResponse<Void>> completeEncounter(@PathVariable Long id) {
        encounterService.completeEncounter(id);
        return ResponseEntity.ok(ApiResponse.success("Encounter completed successfully", HttpStatus.OK.value()));
    }

    /**
     * Cancels an encounter — marks visit as cancelled.
     * Called when patient doesn't show up or visit is cancelled.
     * Status changes: IN_PROGRESS → CANCELLED
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an encounter")
    public ResponseEntity<ApiResponse<Void>> cancelEncounter(@PathVariable Long id) {
        encounterService.cancelEncounter(id);
        return ResponseEntity.ok(ApiResponse.success("Encounter cancelled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a single encounter by ID.
     * Returns full encounter details including diagnoses, prescriptions, observations.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get encounter by ID")
    public ResponseEntity<ApiResponse<EncounterResponseDto>> getEncounterById(@PathVariable Long id) {
        EncounterResponseDto encounter = encounterService.getEncounterById(id);
        return ResponseEntity.ok(ApiResponse.success(encounter, "Encounter retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all encounters for a patient (newest first).
     * Used by: doctor viewing patient history, patient viewing their own history.
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get encounters by patient ID")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByPatientId(@PathVariable Long patientId) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all encounters for a doctor (newest first).
     * Used by: doctor dashboard, viewing visit history.
     */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get encounters by doctor ID")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByDoctorId(@PathVariable Long doctorId) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets encounters by status (IN_PROGRESS, COMPLETED, CANCELLED).
     * Used by: admin dashboard, filtering active encounters.
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get encounters by status")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByStatus(@PathVariable EncounterStatus status) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets today's encounters.
     * Used by: doctor dashboard, receptionist view.
     */
    @GetMapping("/today")
    @Operation(summary = "Get today's encounters")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getTodayEncounters() {
        List<EncounterResponseDto> encounters = encounterService.getTodayEncounters();
        return ResponseEntity.ok(ApiResponse.success(encounters, "Today's encounters retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets encounters within a date range.
     * Used by: reports, statistics, date-based filtering.
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get encounters by date range")
    public ResponseEntity<ApiResponse<List<EncounterResponseDto>>> getEncountersByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        List<EncounterResponseDto> encounters = encounterService.getEncountersByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(encounters, "Encounters retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Count ====================

    /**
     * Counts total encounters for a patient.
     * Used for: patient statistics card.
     */
    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count encounters by patient ID")
    public ResponseEntity<ApiResponse<Long>> countEncountersByPatientId(@PathVariable Long patientId) {
        Long count = encounterService.countEncountersByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Encounters count retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Counts total encounters for a doctor.
     * Used for: doctor dashboard statistics.
     */
    @GetMapping("/count/doctor/{doctorId}")
    @Operation(summary = "Count encounters by doctor ID")
    public ResponseEntity<ApiResponse<Long>> countEncountersByDoctorId(@PathVariable Long doctorId) {
        Long count = encounterService.countEncountersByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(count, "Encounters count retrieved successfully", HttpStatus.OK.value()));
    }
}
