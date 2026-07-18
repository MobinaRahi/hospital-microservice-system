package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.department.DepartmentUpdateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Department management.
 * Handles CRUD, leadership assignment, and member management.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "Department CRUD and management APIs")
public class DepartmentApi {

    private final DepartmentService departmentService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(@Valid @RequestBody DepartmentCreateDto departmentDto) {
        DepartmentResponseDto created = departmentService.createDepartment(departmentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Department created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateDto departmentDto) {
        DepartmentResponseDto updated = departmentService.updateDepartment(id, departmentDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Department updated successfully with id: " + id, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a department by ID")
    public ResponseEntity<ApiResponse<Void>> deactivateDepartment(@PathVariable Long id) {
        departmentService.deactivateDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deactivated successfully with id: " + id, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a department by ID")
    public ResponseEntity<ApiResponse<Void>> activateDepartment(@PathVariable Long id) {
        departmentService.activateDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department activated successfully with id: " + id, HttpStatus.OK.value()));
    }

    // ========== Leadership Management ==========

    @PutMapping("/head-doctor/assign/{departmentId}")
    @Operation(summary = "Assign head doctor to department")
    public ResponseEntity<ApiResponse<Void>> assignHeadDoctor(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.assignHeadDoctor(departmentId, doctorId);
        return ResponseEntity.ok(ApiResponse.success("Head doctor assigned successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/head-nurse/assign/{departmentId}")
    @Operation(summary = "Assign head nurse to department")
    public ResponseEntity<ApiResponse<Void>> assignHeadNurse(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.assignHeadNurse(departmentId, nurseId);
        return ResponseEntity.ok(ApiResponse.success("Head nurse assigned successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/head-doctor/remove/{departmentId}")
    @Operation(summary = "Remove head doctor from department")
    public ResponseEntity<ApiResponse<Void>> removeHeadDoctor(@PathVariable Long departmentId) {
        departmentService.removeHeadDoctor(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Head doctor removed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/head-nurse/remove/{departmentId}")
    @Operation(summary = "Remove head nurse from department")
    public ResponseEntity<ApiResponse<Void>> removeHeadNurse(@PathVariable Long departmentId) {
        departmentService.removeHeadNurse(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Head nurse removed successfully", HttpStatus.OK.value()));
    }

    // ========== Member Management (Doctors) ==========

    @PatchMapping("/doctor/add/{departmentId}")
    @Operation(summary = "Add a doctor to department")
    public ResponseEntity<ApiResponse<Void>> addDoctorToDepartment(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.addDoctorToDepartment(departmentId, doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor added successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/doctor/remove/{departmentId}")
    @Operation(summary = "Remove a doctor from department")
    public ResponseEntity<ApiResponse<Void>> removeDoctorFromDepartment(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.removeDoctorFromDepartment(departmentId, doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor removed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/doctor/remove-all/{departmentId}")
    @Operation(summary = "Remove all doctors from department")
    public ResponseEntity<ApiResponse<Void>> removeAllDoctorsFromDepartment(@PathVariable Long departmentId) {
        departmentService.removeAllDoctorsFromDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("All doctors removed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/{departmentId}")
    @Operation(summary = "Get doctors by department ID")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsByDepartmentId(@PathVariable Long departmentId) {
        List<DoctorResponseDto> doctors = departmentService.getDoctorsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(doctors, "Doctors retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Member Management (Nurses) ==========

    @PatchMapping("/nurse/add/{departmentId}")
    @Operation(summary = "Add a nurse to department")
    public ResponseEntity<ApiResponse<Void>> addNurseToDepartment(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.addNurseToDepartment(departmentId, nurseId);
        return ResponseEntity.ok(ApiResponse.success("Nurse added successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/nurse/remove/{departmentId}")
    @Operation(summary = "Remove a nurse from department")
    public ResponseEntity<ApiResponse<Void>> removeNurseFromDepartment(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.removeNurseFromDepartment(departmentId, nurseId);
        return ResponseEntity.ok(ApiResponse.success("Nurse removed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/nurse/remove-all/{departmentId}")
    @Operation(summary = "Remove all nurses from department")
    public ResponseEntity<ApiResponse<Void>> removeAllNursesFromDepartment(@PathVariable Long departmentId) {
        departmentService.removeAllNursesFromDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("All nurses removed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/nurse/{departmentId}")
    @Operation(summary = "Get nurses by department ID")
    public ResponseEntity<ApiResponse<List<NurseResponseDto>>> getNursesByDepartmentId(@PathVariable Long departmentId) {
        List<NurseResponseDto> nurses = departmentService.getNursesByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(nurses, "Nurses retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Member Management (Rooms) ==========

    @PatchMapping("/room/add/{departmentId}")
    @Operation(summary = "Add a room to department")
    public ResponseEntity<ApiResponse<Void>> addRoomToDepartment(@PathVariable Long departmentId, @RequestBody Long roomId) {
        departmentService.addRoomToDepartment(departmentId, roomId);
        return ResponseEntity.ok(ApiResponse.success("Room added successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/room/remove/{departmentId}")
    @Operation(summary = "Remove a room from department")
    public ResponseEntity<ApiResponse<Void>> removeRoomFromDepartment(@PathVariable Long departmentId, @RequestBody Long roomId) {
        departmentService.removeRoomFromDepartment(departmentId, roomId);
        return ResponseEntity.ok(ApiResponse.success("Room removed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/room/{departmentId}")
    @Operation(summary = "Get rooms by department ID")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByDepartmentId(@PathVariable Long departmentId) {
        List<RoomResponseDto> rooms = departmentService.getRoomsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Head Retrieval ==========

    @GetMapping("/head-doctor/{departmentId}")
    @Operation(summary = "Get head doctor of department")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDepartmentHeadDoctor(@PathVariable Long departmentId) {
        DoctorResponseDto doctor = departmentService.getDepartmentHeadDoctor(departmentId);
        return ResponseEntity.ok(ApiResponse.success(doctor, "Head doctor retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/head-nurse/{departmentId}")
    @Operation(summary = "Get head nurse of department")
    public ResponseEntity<ApiResponse<NurseResponseDto>> getDepartmentHeadNurse(@PathVariable Long departmentId) {
        NurseResponseDto nurse = departmentService.getDepartmentHeadNurse(departmentId);
        return ResponseEntity.ok(ApiResponse.success(nurse, "Head nurse retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department, "Department retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-code")
    @Operation(summary = "Get department by code")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentByCode(@RequestParam String code) {
        DepartmentResponseDto department = departmentService.getDepartmentByCode(code);
        return ResponseEntity.ok(ApiResponse.success(department, "Department retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-code")
    @Operation(summary = "Get active department by code")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getActiveDepartmentByCode(@RequestParam String code) {
        DepartmentResponseDto department = departmentService.getDepartmentByCodeAndIsActiveTrue(code);
        return ResponseEntity.ok(ApiResponse.success(department, "Active department retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get departments by name")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentByName(name);
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-name")
    @Operation(summary = "Get active departments by name")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getActiveDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentByNameAndIsActiveTru(name);
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getActiveDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getInactiveDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getInactiveDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments, "Inactive departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments, "All departments retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Search ==========

    @GetMapping("/search/by-name")
    @Operation(summary = "Search departments by name (partial match, case-insensitive)")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> searchDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByName(name);
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search/by-location")
    @Operation(summary = "Search departments by location (partial match, case-insensitive)")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> searchDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-location")
    @Operation(summary = "Get departments by exact location")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-status")
    @Operation(summary = "Get departments by status")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getDepartmentsByStatus(@RequestParam boolean isActive) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByStatus(isActive);
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search/active/by-name")
    @Operation(summary = "Search active departments by name")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> searchActiveDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByNameAndIsActiveTrue(name);
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search/active/by-location")
    @Operation(summary = "Search active departments by location")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> searchActiveDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByLocationAndIsActiveTrue(location);
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-location")
    @Operation(summary = "Get active departments by exact location")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getActiveDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByLocationAndIsActiveTrue(location);
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics ==========

    @GetMapping("/count/doctors")
    @Operation(summary = "Count doctors in a department")
    public ResponseEntity<ApiResponse<Long>> countDoctorsInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countDoctorsInDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Doctor count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/nurses")
    @Operation(summary = "Count nurses in a department")
    public ResponseEntity<ApiResponse<Long>> countNursesInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countNursesInDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Nurse count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/rooms")
    @Operation(summary = "Count rooms in a department")
    public ResponseEntity<ApiResponse<Long>> countRoomsInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countRoomsInDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Room count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/total")
    @Operation(summary = "Count total departments")
    public ResponseEntity<ApiResponse<Long>> countTotalDepartments() {
        Long count = departmentService.countTotalDepartments();
        return ResponseEntity.ok(ApiResponse.success(count, "Total departments count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/active")
    @Operation(summary = "Count active departments")
    public ResponseEntity<ApiResponse<Long>> countActiveDepartments() {
        Long count = departmentService.countActiveDepartments();
        return ResponseEntity.ok(ApiResponse.success(count, "Active departments count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/inactive")
    @Operation(summary = "Count inactive departments")
    public ResponseEntity<ApiResponse<Long>> countInactiveDepartments() {
        Long count = departmentService.countInactiveDepartments();
        return ResponseEntity.ok(ApiResponse.success(count, "Inactive departments count retrieved successfully", HttpStatus.OK.value()));
    }
}