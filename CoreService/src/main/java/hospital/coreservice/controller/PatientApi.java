package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "Patient CRUD and management APIs")
public class PatientApi {

    private final PatientService patientService;

    // ========== Core Operations (Create, Update, Patch, Delete) ==========

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<ApiResponse> createPatient(@Valid @RequestBody PatientCreateDto patientCreateDto) {
        PatientResponseDto created = patientService.createPatient(patientCreateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Patient created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient by ID")
    public ResponseEntity<ApiResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateDto updateDto) {
        PatientResponseDto updated = patientService.updatePatient(id, updateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patch/{id}")
    @Operation(summary = "Partially update a patient")
    public ResponseEntity<ApiResponse> patchPatient(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {
        PatientResponseDto patched = patientService.patchPatient(id, updates);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient partially updated successfully")
                .data(patched)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Soft delete a patient (deactivate)")
    public ResponseEntity<ApiResponse> deactivatePatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient deactivated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a patient")
    public ResponseEntity<ApiResponse> activatePatient(@PathVariable Long id) {
        patientService.activatePatient(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient activated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/archive")
    @Operation(summary = "Archive multiple patients (batch soft delete)")
    public ResponseEntity<ApiResponse> archivePatients(@RequestBody List<Long> patientIds) {
        patientService.archivePatients(patientIds);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients archived successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable Long id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient retrieved successfully")
                .data(patient)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-national-id")
    @Operation(summary = "Get patient by national ID")
    public ResponseEntity<ApiResponse> getPatientByNationalId(@RequestParam String nationalId) {
        PatientResponseDto patient = patientService.getPatientByNationalId(nationalId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient retrieved by national ID successfully")
                .data(patient)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-phone-number")
    @Operation(summary = "Get patient by phone number")
    public ResponseEntity<ApiResponse> getPatientByPhoneNumber(@RequestParam String phoneNumber) {
        PatientResponseDto patient = patientService.getPatientByPhoneNumber(phoneNumber);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient retrieved by phone number successfully")
                .data(patient)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<ApiResponse> getAllPatients() {
        List<PatientResponseDto> patients = patientService.getAllPatients();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All patients retrieved successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Search & Filter ==========

    @GetMapping("/by-first-name")
    @Operation(summary = "Search patients by first name (partial, case-insensitive)")
    public ResponseEntity<ApiResponse> getPatientsByFirstNameContainingIgnoreCase(@RequestParam String firstName) {
        List<PatientResponseDto> patients = patientService.getPatientsByFirstNameContainingIgnoreCase(firstName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients found by first name successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-last-name")
    @Operation(summary = "Search patients by last name (partial, case-insensitive)")
    public ResponseEntity<ApiResponse> getPatientsByLastNameContainingIgnoreCase(@RequestParam String lastName) {
        List<PatientResponseDto> patients = patientService.getPatientsByLastNameContainingIgnoreCase(lastName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients found by last name successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-first-name-last-name")
    @Operation(summary = "Search patients by first name and last name")
    public ResponseEntity<ApiResponse> getPatientsByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<PatientResponseDto> patients = patientService.getPatientsByFirstNameAndLastName(firstName, lastName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients found by full name successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Advanced search with optional filters")
    public ResponseEntity<ApiResponse> searchPatients(
            @RequestParam(required = false) String nationalId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) PatientStatus status) {
        List<PatientResponseDto> patients = patientService.searchPatients(nationalId, firstName, lastName, status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Advanced search completed successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-gender")
    @Operation(summary = "Get patients by gender")
    public ResponseEntity<ApiResponse> getPatientsByGender(@RequestParam Gender gender) {
        List<PatientResponseDto> patients = patientService.getPatientsByGender(gender);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients filtered by gender successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-blood-type")
    @Operation(summary = "Get patients by blood type")
    public ResponseEntity<ApiResponse> getPatientsByBloodType(@RequestParam BloodType bloodType) {
        List<PatientResponseDto> patients = patientService.getPatientsByBloodType(bloodType);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients filtered by blood type successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Get patients by status (without pagination)")
    public ResponseEntity<ApiResponse> getPatientsByStatus(@RequestParam PatientStatus status) {
        List<PatientResponseDto> patients = patientService.getPatientsByStatus(status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients filtered by status successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status-pageable")
    @Operation(summary = "Get patients by status with pagination")
    public ResponseEntity<ApiResponse> getPatientsByStatus(@RequestParam PatientStatus status, Pageable pageable) {
        List<PatientResponseDto> patients = patientService.getPatientsByStatus(status, pageable);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients filtered by status (paginated) successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-birth-date-between")
    @Operation(summary = "Get patients by birth date range")
    public ResponseEntity<ApiResponse> getPatientsByBirthDateBetween(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<PatientResponseDto> patients = patientService.getPatientsByBirthDateBetween(start, end);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients found by birth date range successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Room Assignment ==========

    @GetMapping("/by-current-room-id")
    @Operation(summary = "Get patients currently in a specific room")
    public ResponseEntity<ApiResponse> getPatientsByCurrentRoomId(@RequestParam Long roomId) {
        List<PatientResponseDto> patients = patientService.getPatientsByCurrentRoomId(roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patients in room retrieved successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/assign/room/{patientId}")
    @Operation(summary = "Assign a patient to a room")
    public ResponseEntity<ApiResponse> assignRoom(@RequestParam Long roomId, @PathVariable Long patientId) {
        patientService.assignRoom(patientId, roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room assigned successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/unassign/room/{patientId}")
    @Operation(summary = "Remove a patient from their current room")
    public ResponseEntity<ApiResponse> unassignRoom(@PathVariable Long patientId) {
        patientService.unassignRoom(patientId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room unassigned successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========

    @GetMapping("/count/by-status")
    @Operation(summary = "Count patients by status")
    public ResponseEntity<ApiResponse> countPatientsByStatus(@RequestParam PatientStatus status) {
        Long count = patientService.countPatientsByStatus(status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient count by status retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/by-gender")
    @Operation(summary = "Count patients by gender")
    public ResponseEntity<ApiResponse> countPatientsByGender(@RequestParam Gender gender) {
        Long count = patientService.countPatientsByGender(gender);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient count by gender retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/by-blood-type")
    @Operation(summary = "Count patients by blood type")
    public ResponseEntity<ApiResponse> countPatientsByBloodType(@RequestParam BloodType bloodType) {
        Long count = patientService.countPatientsByBloodType(bloodType);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient count by blood type retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active patients")
    public ResponseEntity<ApiResponse> countActivePatients() {
        Long count = patientService.countActivePatients();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active patients count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}