package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.nurse.NurseUpdateDto;
import hospital.coreservice.model.enums.NursePosition;
import hospital.coreservice.service.NurseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Nurse management.
 * Handles CRUD, department assignment, and shift management.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/nurse")
@RequiredArgsConstructor
@Tag(name = "Nurse Management", description = "Nurse CRUD and management APIs")
public class NurseApi {

    private final NurseService nurseService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new nurse")
    public ResponseEntity<ApiResponse<NurseResponseDto>> createNurse(@Valid @RequestBody NurseCreateDto nurse) {
        NurseResponseDto created = nurseService.createNurse(nurse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Nurse created successfully", HttpStatus.CREATED.value()));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create nurses")
    public ResponseEntity<ApiResponse<Void>> bulkCreateNurses(@Valid @RequestBody List<NurseCreateDto> nurseCreateDtoList) {
        nurseService.bulkCreateNurses(nurseCreateDtoList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Nurses created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{nurseId}")
    @Operation(summary = "Update a nurse by ID")
    public ResponseEntity<ApiResponse<NurseResponseDto>> updateNurse(@PathVariable Long nurseId, @Valid @RequestBody NurseUpdateDto nurse) {
        NurseResponseDto updated = nurseService.updateNurse(nurseId, nurse);
        return ResponseEntity.ok(ApiResponse.success(updated, "Nurse updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/deactivate/{nurseId}")
    @Operation(summary = "Deactivate a nurse by ID")
    public ResponseEntity<ApiResponse<Void>> deactivateNurse(@PathVariable Long nurseId) {
        nurseService.deactivateNurse(nurseId);
        return ResponseEntity.ok(ApiResponse.success("Nurse deactivated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/activate/{nurseId}")
    @Operation(summary = "Activate a nurse by ID")
    public ResponseEntity<ApiResponse<Void>> activateNurse(@PathVariable Long nurseId) {
        nurseService.activateNurse(nurseId);
        return ResponseEntity.ok(ApiResponse.success("Nurse activated successfully", HttpStatus.OK.value()));
    }

    // ========== Department Assignment ==========

    @PatchMapping("/assign/department/{nurseId}")
    @Operation(summary = "Assign department to nurse")
    public ResponseEntity<ApiResponse<Void>> assignDepartment(@PathVariable Long nurseId, @RequestParam Long departmentId) {
        nurseService.assignDepartment(nurseId, departmentId);
        return ResponseEntity.ok(ApiResponse.success("Department assigned to nurse successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/remove/department/{nurseId}")
    @Operation(summary = "Remove department from nurse")
    public ResponseEntity<ApiResponse<Void>> removeDepartment(@PathVariable Long nurseId, @RequestParam Long departmentId) {
        nurseService.removeDepartment(nurseId, departmentId);
        return ResponseEntity.ok(ApiResponse.success("Department removed from nurse successfully", HttpStatus.OK.value()));
    }

    // ========== Shift Preference Management ==========

    @PatchMapping("/add/shift/{nurseId}")
    @Operation(summary = "Add shift preference to nurse")
    public ResponseEntity<ApiResponse<Void>> addShiftPreference(@PathVariable Long nurseId, @RequestParam Long shiftId) {
        nurseService.addShiftPreference(nurseId, shiftId);
        return ResponseEntity.ok(ApiResponse.success("Shift preference added successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/remove/shift/{nurseId}")
    @Operation(summary = "Remove shift preference from nurse")
    public ResponseEntity<ApiResponse<Void>> removeShiftPreference(@PathVariable Long nurseId, @RequestParam Long shiftId) {
        nurseService.removeShiftPreference(nurseId, shiftId);
        return ResponseEntity.ok(ApiResponse.success("Shift preference removed successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{nurseId}")
    @Operation(summary = "Get a nurse by ID")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseById(@PathVariable Long nurseId) {
        NurseResponseDto nurse = nurseService.getNurseById(nurseId);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-user-id")
    @Operation(summary = "Get a nurse by user ID")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseByUserId(@RequestParam Long userId) {
        NurseResponseDto nurse = nurseService.getNurseByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse retrieved by user ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-nurse-code")
    @Operation(summary = "Get a nurse by nurse code")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseByNurseCode(@RequestParam String nurseCode) {
        NurseResponseDto nurse = nurseService.getNurseByNurseCode(nurseCode);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse retrieved by nurse code successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-national-id")
    @Operation(summary = "Get a nurse by national ID")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseByNationalId(@RequestParam String nationalId) {
        NurseResponseDto nurse = nurseService.getNurseByNationalId(nationalId);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse retrieved by national ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-phone-number")
    @Operation(summary = "Get a nurse by phone number")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseByPhoneNumber(@RequestParam String phoneNumber) {
        NurseResponseDto nurse = nurseService.getNurseByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse retrieved by phone number successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/with-shifts/{nurseId}")
    @Operation(summary = "Get a nurse with shift preferences")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getNurseWithShifts(@PathVariable Long nurseId) {
        NurseResponseDto nurse = nurseService.getNurseWithShifts(nurseId);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Nurse with shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all nurses")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getAllNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllNurses();
        return ResponseEntity.ok(ApiResponse.success(nurses, "All nurses retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Search & Filter ==========

    @GetMapping("/search/by-name")
    @Operation(summary = "Search nurses by first name and last name")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> searchNursesByName(@RequestParam String firstName, @RequestParam String lastName) {
        List<NurseResponseDto> nurses = nurseService.searchNursesByName(firstName, lastName);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Nurses searched by name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/search/by-name")
    @Operation(summary = "Search active nurses by name")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> searchActiveNursesByName(@RequestParam String firstName, @RequestParam String lastName) {
        List<NurseResponseDto> nurses = nurseService.searchActiveNursesByName(firstName, lastName);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Active nurses searched by name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-position")
    @Operation(summary = "Get nurses by position")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getNursesByPosition(@RequestParam NursePosition position) {
        List<NurseResponseDto> nurses = nurseService.getNursesByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Nurses retrieved by position successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-position")
    @Operation(summary = "Get active nurses by position")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getActiveNursesByPosition(@RequestParam NursePosition position) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Active nurses retrieved by position successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-department-id")
    @Operation(summary = "Get nurses by department ID")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getNursesByDepartmentId(@RequestParam Long departmentId) {
        List<NurseResponseDto> nurses = nurseService.getNursesByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Nurses retrieved by department ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-department-id")
    @Operation(summary = "Get active nurses by department ID")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getActiveNursesByDepartmentId(@RequestParam Long departmentId) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Active nurses retrieved by department ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-experience-range")
    @Operation(summary = "Get nurses by years of experience range")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getNursesByExperienceRange(@RequestParam int min, @RequestParam int max) {
        List<NurseResponseDto> nurses = nurseService.getNursesByExperienceRange(min, max);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Nurses retrieved by experience range successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-experience-range")
    @Operation(summary = "Get active nurses by years of experience range")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getActiveNursesByExperienceRange(@RequestParam int min, @RequestParam int max) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByExperienceRange(min, max);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Active nurses retrieved by experience range successfully", HttpStatus.OK.value()));
    }

    // ========== Active / Inactive Lists ==========

    @GetMapping("/active")
    @Operation(summary = "Get all active nurses")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getActiveNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllActiveNurses();
        return ResponseEntity.ok(ApiResponse.success(nurses, "Active nurses retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive nurses")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getInactiveNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllInactiveNurses();
        return ResponseEntity.ok(ApiResponse.success(nurses, "Inactive nurses retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total nurses")
    public ResponseEntity<ApiResponse<Long>> countAllNurses() {
        Long count = nurseService.countAllNurses();
        return ResponseEntity.ok(ApiResponse.success(count, "Total nurses count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-position")
    @Operation(summary = "Count nurses by position")
    public ResponseEntity<ApiResponse<Long>> countNursesByPosition(@RequestParam NursePosition position) {
        Long count = nurseService.countNursesByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(count, "Nurses count by position retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-department-id")
    @Operation(summary = "Count nurses by department ID")
    public ResponseEntity<ApiResponse<Long>> countNursesByDepartmentId(@RequestParam Long departmentId) {
        Long count = nurseService.countNursesByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Nurses count by department ID retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active nurses")
    public ResponseEntity<ApiResponse<Long>> countActiveNurses() {
        Long count = nurseService.countActiveNurses();
        return ResponseEntity.ok(ApiResponse.success(count, "Active nurses count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive/count")
    @Operation(summary = "Count inactive nurses")
    public ResponseEntity<ApiResponse<Long>> countInactiveNurses() {
        Long count = nurseService.countInactiveNurses();
        return ResponseEntity.ok(ApiResponse.success(count, "Inactive nurses count retrieved successfully", HttpStatus.OK.value()));
    }
}