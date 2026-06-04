package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/nurse")
@RequiredArgsConstructor
@Tag(name = "Nurse Management", description = "Nurse CRUD and management APIs")
public class NurseApi {

    private final NurseService nurseService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new nurse")
    public ResponseEntity<ApiResponse> createNurse(@Valid @RequestBody NurseCreateDto nurse) {
        NurseResponseDto created = nurseService.createNurse(nurse);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Nurse created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create nurses")
    public ResponseEntity<ApiResponse> bulkCreateNurses(@Valid @RequestBody List<NurseCreateDto> nurseCreateDtoList) {
       nurseService.bulkCreateNurses(nurseCreateDtoList);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Nurses created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{nurseId}")
    @Operation(summary = "Update a nurse by ID")
    public ResponseEntity<ApiResponse> updateNurse(@PathVariable Long nurseId, @Valid @RequestBody NurseUpdateDto nurse) {
        NurseResponseDto updated = nurseService.updateNurse(nurseId, nurse);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deactivate/{nurseId}")
    @Operation(summary = "Deactivate a nurse by ID")
    public ResponseEntity<ApiResponse> deactivateNurse(@PathVariable Long nurseId) {
        nurseService.deactivateNurse(nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse deactivated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate/{nurseId}")
    @Operation(summary = "Activate a nurse by ID")
    public ResponseEntity<ApiResponse> activateNurse(@PathVariable Long nurseId) {
        nurseService.activateNurse(nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse activated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Department Assignment ==========

    @PatchMapping("/assign/department/{nurseId}")
    @Operation(summary = "Assign department to nurse")
    public ResponseEntity<ApiResponse> assignDepartment(@PathVariable Long nurseId, @RequestParam Long departmentId) {
        nurseService.assignDepartment(nurseId, departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department assigned to nurse successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/remove/department/{nurseId}")
    @Operation(summary = "Remove department from nurse")
    public ResponseEntity<ApiResponse> removeDepartment(@PathVariable Long nurseId, @RequestParam Long departmentId) {
        nurseService.removeDepartment(nurseId, departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department removed from nurse successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Shift Preference Management ==========

    @PatchMapping("/add/shift/{nurseId}")
    @Operation(summary = "Add shift preference to nurse")
    public ResponseEntity<ApiResponse> addShiftPreference(@PathVariable Long nurseId, @RequestParam Long shiftId) {
        nurseService.addShiftPreference(nurseId, shiftId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift preference added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/remove/shift/{nurseId}")
    @Operation(summary = "Remove shift preference from nurse")
    public ResponseEntity<ApiResponse> removeShiftPreference(@PathVariable Long nurseId, @RequestParam Long shiftId) {
        nurseService.removeShiftPreference(nurseId, shiftId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift preference removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{nurseId}")
    @Operation(summary = "Get a nurse by ID")
    public ResponseEntity<ApiResponse> getNurseById(@PathVariable Long nurseId) {
        NurseResponseDto nurse = nurseService.getNurseById(nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user-id")
    @Operation(summary = "Get a nurse by user ID")
    public ResponseEntity<ApiResponse> getNurseByUserId(@RequestParam Long userId) {
        NurseResponseDto nurse = nurseService.getNurseByUserId(userId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved by user ID successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-nurse-code")
    @Operation(summary = "Get a nurse by nurse code")
    public ResponseEntity<ApiResponse> getNurseByNurseCode(@RequestParam String nurseCode) {
        NurseResponseDto nurse = nurseService.getNurseByNurseCode(nurseCode);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved by nurse code successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-national-id")
    @Operation(summary = "Get a nurse by national ID")
    public ResponseEntity<ApiResponse> getNurseByNationalId(@RequestParam String nationalId) {
        NurseResponseDto nurse = nurseService.getNurseByNationalId(nationalId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved by national ID successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-phone-number")
    @Operation(summary = "Get a nurse by phone number")
    public ResponseEntity<ApiResponse> getNurseByPhoneNumber(@RequestParam String phoneNumber) {
        NurseResponseDto nurse = nurseService.getNurseByPhoneNumber(phoneNumber);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved by phone number successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-shifts/{nurseId}")
    @Operation(summary = "Get a nurse with shift preferences")
    public ResponseEntity<ApiResponse> getNurseWithShifts(@PathVariable Long nurseId) {
        NurseResponseDto nurse = nurseService.getNurseWithShifts(nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse with shifts retrieved successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all nurses")
    public ResponseEntity<ApiResponse> getAllNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All nurses retrieved successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Search & Filter ==========

    @GetMapping("/search/by-name")
    @Operation(summary = "Search nurses by first name and last name")
    public ResponseEntity<ApiResponse> searchNursesByName(@RequestParam String firstName, @RequestParam String lastName) {
        List<NurseResponseDto> nurses = nurseService.searchNursesByName(firstName, lastName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses searched by name successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/search/by-name")
    @Operation(summary = "Search active nurses by name")
    public ResponseEntity<ApiResponse> searchActiveNursesByName(@RequestParam String firstName, @RequestParam String lastName) {
        List<NurseResponseDto> nurses = nurseService.searchActiveNursesByName(firstName, lastName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses searched by name successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-position")
    @Operation(summary = "Get nurses by position")
    public ResponseEntity<ApiResponse> getNursesByPosition(@RequestParam NursePosition position) {
        List<NurseResponseDto> nurses = nurseService.getNursesByPosition(position);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved by position successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-position")
    @Operation(summary = "Get active nurses by position")
    public ResponseEntity<ApiResponse> getActiveNursesByPosition(@RequestParam NursePosition position) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByPosition(position);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses retrieved by position successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-department-id")
    @Operation(summary = "Get nurses by department ID")
    public ResponseEntity<ApiResponse> getNursesByDepartmentId(@RequestParam Long departmentId) {
        List<NurseResponseDto> nurses = nurseService.getNursesByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved by department ID successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-department-id")
    @Operation(summary = "Get active nurses by department ID")
    public ResponseEntity<ApiResponse> getActiveNursesByDepartmentId(@RequestParam Long departmentId) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses retrieved by department ID successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-experience-range")
    @Operation(summary = "Get nurses by years of experience range")
    public ResponseEntity<ApiResponse> getNursesByExperienceRange(@RequestParam int min, @RequestParam int max) {
        List<NurseResponseDto> nurses = nurseService.getNursesByExperienceRange(min, max);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved by experience range successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-experience-range")
    @Operation(summary = "Get active nurses by years of experience range")
    public ResponseEntity<ApiResponse> getActiveNursesByExperienceRange(@RequestParam int min, @RequestParam int max) {
        List<NurseResponseDto> nurses = nurseService.getActiveNursesByExperienceRange(min, max);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses retrieved by experience range successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Active / Inactive Lists ==========

    @GetMapping("/active")
    @Operation(summary = "Get all active nurses")
    public ResponseEntity<ApiResponse> getActiveNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllActiveNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses retrieved successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive nurses")
    public ResponseEntity<ApiResponse> getInactiveNurses() {
        List<NurseResponseDto> nurses = nurseService.getAllInactiveNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive nurses retrieved successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total nurses")
    public ResponseEntity<ApiResponse> countAllNurses() {
        Long count = nurseService.countAllNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Total nurses count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/by-position")
    @Operation(summary = "Count nurses by position")
    public ResponseEntity<ApiResponse> countNursesByPosition(@RequestParam NursePosition position) {
        Long count = nurseService.countNursesByPosition(position);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses count by position retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/by-department-id")
    @Operation(summary = "Count nurses by department ID")
    public ResponseEntity<ApiResponse> countNursesByDepartmentId(@RequestParam Long departmentId) {
        Long count = nurseService.countNursesByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses count by department ID retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active nurses")
    public ResponseEntity<ApiResponse> countActiveNurses() {
        Long count = nurseService.countActiveNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active nurses count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive/count")
    @Operation(summary = "Count inactive nurses")
    public ResponseEntity<ApiResponse> countInactiveNurses() {
        Long count = nurseService.countInactiveNurses();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive nurses count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}