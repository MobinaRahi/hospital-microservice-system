package hospital.coreservice.controller;

import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.service.PatientService;
import hospital.coreservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserService userService;

    // ========== Core Operations (Create, Update, Patch, Delete) ==========

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<ApiResponse<PatientResponseDto>> createPatient(@Valid @RequestBody PatientCreateDto patientCreateDto) {
        PatientResponseDto created = patientService.createPatient(patientCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Patient created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient by ID")
    public ResponseEntity<ApiResponse<PatientResponseDto>> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateDto updateDto) {
        PatientResponseDto updated = patientService.updatePatient(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Patient updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/patch/{id}")
    @Operation(summary = "Partially update a patient")
    public ResponseEntity<ApiResponse<PatientResponseDto>> patchPatient(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {
        PatientResponseDto patched = patientService.patchPatient(id, updates);
        return ResponseEntity.ok(ApiResponse.success(patched, "Patient partially updated successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/complete-registration")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Complete patient registration with password setup")
    public ResponseEntity<ApiResponse<PatientResponseDto>> completeRegistration(
            @Valid @RequestBody CompleteRegistrationRequest request) {

        // بررسی تطابق رمز عبور
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("رمز عبور و تأیید آن مطابقت ندارند", HttpStatus.BAD_REQUEST.value()));
        }

        PatientResponseDto updated = patientService.completeRegistration(request.getPatientId(), request);

        return ResponseEntity.ok(
                ApiResponse.success(updated, "ثبت‌نام با موفقیت کامل شد", HttpStatus.OK.value())
        );
    }


    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Soft delete a patient (deactivate)")
    public ResponseEntity<ApiResponse<Void>> deactivatePatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deactivated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a patient")
    public ResponseEntity<ApiResponse<Void>> activatePatient(@PathVariable Long id) {
        patientService.activatePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient activated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/archive")
    @Operation(summary = "Archive multiple patients (batch soft delete)")
    public ResponseEntity<ApiResponse<Void>> archivePatients(@RequestBody List<Long> patientIds) {
        patientService.archivePatients(patientIds);
        return ResponseEntity.ok(ApiResponse.success("Patients archived successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientResponseDto>> getPatientById(@PathVariable Long id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success(patient, "Patient retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-national-id")
    @Operation(summary = "Get patient by national ID")
    public ResponseEntity<ApiResponse<PatientResponseDto>> getPatientByNationalId(@RequestParam String nationalId) {
        PatientResponseDto patient = patientService.getPatientByNationalId(nationalId);
        return ResponseEntity.ok(ApiResponse.success(patient, "Patient retrieved by national ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-phone-number")
    @Operation(summary = "Get patient by phone number")
    public ResponseEntity<ApiResponse<PatientResponseDto>> getPatientByPhoneNumber(@RequestParam String phoneNumber) {
        PatientResponseDto patient = patientService.getPatientByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(ApiResponse.success(patient, "Patient retrieved by phone number successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getAllPatients() {
        List<PatientResponseDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.success(patients, "All patients retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Search & Filter ==========

    @GetMapping("/by-first-name")
    @Operation(summary = "Search patients by first name (partial, case-insensitive)")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByFirstNameContainingIgnoreCase(@RequestParam String firstName) {
        List<PatientResponseDto> patients = patientService.getPatientsByFirstNameContainingIgnoreCase(firstName);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found by first name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-last-name")
    @Operation(summary = "Search patients by last name (partial, case-insensitive)")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByLastNameContainingIgnoreCase(@RequestParam String lastName) {
        List<PatientResponseDto> patients = patientService.getPatientsByLastNameContainingIgnoreCase(lastName);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found by last name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-first-name-last-name")
    @Operation(summary = "Search patients by first name and last name")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<PatientResponseDto> patients = patientService.getPatientsByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found by full name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Advanced search with optional filters")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> searchPatients(
            @RequestParam(required = false) String nationalId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) PatientStatus status) {
        List<PatientResponseDto> patients = patientService.searchPatients(nationalId, firstName, lastName, status);
        return ResponseEntity.ok(ApiResponse.success(patients, "Advanced search completed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-gender")
    @Operation(summary = "Get patients by gender")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByGender(@RequestParam Gender gender) {
        List<PatientResponseDto> patients = patientService.getPatientsByGender(gender);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients filtered by gender successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-blood-type")
    @Operation(summary = "Get patients by blood type")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByBloodType(@RequestParam BloodType bloodType) {
        List<PatientResponseDto> patients = patientService.getPatientsByBloodType(bloodType);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients filtered by blood type successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-status")
    @Operation(summary = "Get patients by status (without pagination)")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByStatus(@RequestParam PatientStatus status) {
        List<PatientResponseDto> patients = patientService.getPatientsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients filtered by status successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-status-pageable")
    @Operation(summary = "Get patients by status with pagination")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByStatus(@RequestParam PatientStatus status, Pageable pageable) {
        List<PatientResponseDto> patients = patientService.getPatientsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients filtered by status (paginated) successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-birth-date-between")
    @Operation(summary = "Get patients by birth date range")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByBirthDateBetween(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<PatientResponseDto> patients = patientService.getPatientsByBirthDateBetween(start, end);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found by birth date range successfully", HttpStatus.OK.value()));
    }

    // ========== Room Assignment ==========

    @GetMapping("/by-current-room-id")
    @Operation(summary = "Get patients currently in a specific room")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByCurrentRoomId(@RequestParam Long roomId) {
        List<PatientResponseDto> patients = patientService.getPatientsByCurrentRoomId(roomId);
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients in room retrieved successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/assign/room/{patientId}")
    @Operation(summary = "Assign a patient to a room")
    public ResponseEntity<ApiResponse<Void>> assignRoom(@RequestParam Long roomId, @PathVariable Long patientId) {
        patientService.assignRoom(patientId, roomId);
        return ResponseEntity.ok(ApiResponse.success("Room assigned successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/unassign/room/{patientId}")
    @Operation(summary = "Remove a patient from their current room")
    public ResponseEntity<ApiResponse<Void>> unassignRoom(@PathVariable Long patientId) {
        patientService.unassignRoom(patientId);
        return ResponseEntity.ok(ApiResponse.success("Room unassigned successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics ==========

    @GetMapping("/count/by-status")
    @Operation(summary = "Count patients by status")
    public ResponseEntity<ApiResponse<Long>> countPatientsByStatus(@RequestParam PatientStatus status) {
        Long count = patientService.countPatientsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count, "Patient count by status retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-gender")
    @Operation(summary = "Count patients by gender")
    public ResponseEntity<ApiResponse<Long>> countPatientsByGender(@RequestParam Gender gender) {
        Long count = patientService.countPatientsByGender(gender);
        return ResponseEntity.ok(ApiResponse.success(count, "Patient count by gender retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-blood-type")
    @Operation(summary = "Count patients by blood type")
    public ResponseEntity<ApiResponse<Long>> countPatientsByBloodType(@RequestParam BloodType bloodType) {
        Long count = patientService.countPatientsByBloodType(bloodType);
        return ResponseEntity.ok(ApiResponse.success(count, "Patient count by blood type retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active patients")
    public ResponseEntity<ApiResponse<Long>> countActivePatients() {
        Long count = patientService.countActivePatients();
        return ResponseEntity.ok(ApiResponse.success(count, "Active patients count retrieved successfully", HttpStatus.OK.value()));
    }
}