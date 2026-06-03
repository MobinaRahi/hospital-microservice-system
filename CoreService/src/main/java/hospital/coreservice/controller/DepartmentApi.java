package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "Department CRUD and management APIs")
public class DepartmentApi {

    private final DepartmentService departmentService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentCreateDto departmentDto) {
        DepartmentResponseDto created = departmentService.createDepartment(departmentDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Department created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a department by ID")
    public ResponseEntity<ApiResponse> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateDto departmentDto) {
        DepartmentResponseDto updated = departmentService.updateDepartment(id, departmentDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department updated successfully with id: " + id)
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a department by ID")
    public ResponseEntity<ApiResponse> deactivateDepartment(@PathVariable Long id) {
        departmentService.deactivateDepartment(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department deactivated successfully with id: " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a department by ID")
    public ResponseEntity<ApiResponse> activateDepartment(@PathVariable Long id) {
        departmentService.activateDepartment(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department activated successfully with id: " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Leadership Management ==========

    @PutMapping("/head-doctor/assign/{departmentId}")
    @Operation(summary = "Assign head doctor to department")
    public ResponseEntity<ApiResponse> assignHeadDoctor(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.assignHeadDoctor(departmentId, doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head doctor assigned successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/head-nurse/assign/{departmentId}")
    @Operation(summary = "Assign head nurse to department")
    public ResponseEntity<ApiResponse> assignHeadNurse(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.assignHeadNurse(departmentId, nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head nurse assigned successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/head-doctor/remove/{departmentId}")
    @Operation(summary = "Remove head doctor from department")
    public ResponseEntity<ApiResponse> removeHeadDoctor(@PathVariable Long departmentId) {
        departmentService.removeHeadDoctor(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head doctor removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/head-nurse/remove/{departmentId}")
    @Operation(summary = "Remove head nurse from department")
    public ResponseEntity<ApiResponse> removeHeadNurse(@PathVariable Long departmentId) {
        departmentService.removeHeadNurse(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head nurse removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Member Management (Doctors) ==========

    @PatchMapping("/doctor/add/{departmentId}")
    @Operation(summary = "Add a doctor to department")
    public ResponseEntity<ApiResponse> addDoctorToDepartment(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.addDoctorToDepartment(departmentId, doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/doctor/remove/{departmentId}")
    @Operation(summary = "Remove a doctor from department")
    public ResponseEntity<ApiResponse> removeDoctorFromDepartment(@PathVariable Long departmentId, @RequestBody Long doctorId) {
        departmentService.removeDoctorFromDepartment(departmentId, doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/doctor/remove-all/{departmentId}")
    @Operation(summary = "Remove all doctors from department")
    public ResponseEntity<ApiResponse> removeAllDoctorsFromDepartment(@PathVariable Long departmentId) {
        departmentService.removeAllDoctorsFromDepartment(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All doctors removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{departmentId}")
    @Operation(summary = "Get doctors by department ID")
    public ResponseEntity<ApiResponse> getDoctorsByDepartmentId(@PathVariable Long departmentId) {
        List<DoctorResponseDto> doctors = departmentService.getDoctorsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctors retrieved successfully")
                .data(doctors)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Member Management (Nurses) ==========

    @PatchMapping("/nurse/add/{departmentId}")
    @Operation(summary = "Add a nurse to department")
    public ResponseEntity<ApiResponse> addNurseToDepartment(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.addNurseToDepartment(departmentId, nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nurse/remove/{departmentId}")
    @Operation(summary = "Remove a nurse from department")
    public ResponseEntity<ApiResponse> removeNurseFromDepartment(@PathVariable Long departmentId, @RequestBody Long nurseId) {
        departmentService.removeNurseFromDepartment(departmentId, nurseId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nurse/remove-all/{departmentId}")
    @Operation(summary = "Remove all nurses from department")
    public ResponseEntity<ApiResponse> removeAllNursesFromDepartment(@PathVariable Long departmentId) {
        departmentService.removeAllNursesFromDepartment(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All nurses removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nurse/{departmentId}")
    @Operation(summary = "Get nurses by department ID")
    public ResponseEntity<ApiResponse> getNursesByDepartmentId(@PathVariable Long departmentId) {
        List<NurseResponseDto> nurses = departmentService.getNursesByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved successfully")
                .data(nurses)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Member Management (Rooms) ==========

    @PatchMapping("/room/add/{departmentId}")
    @Operation(summary = "Add a room to department")
    public ResponseEntity<ApiResponse> addRoomToDepartment(@PathVariable Long departmentId, @RequestBody Long roomId) {
        departmentService.addRoomToDepartment(departmentId, roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/room/remove/{departmentId}")
    @Operation(summary = "Remove a room from department")
    public ResponseEntity<ApiResponse> removeRoomFromDepartment(@PathVariable Long departmentId, @RequestBody Long roomId) {
        departmentService.removeRoomFromDepartment(departmentId, roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room removed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/{departmentId}")
    @Operation(summary = "Get rooms by department ID")
    public ResponseEntity<ApiResponse> getRoomsByDepartmentId(@PathVariable Long departmentId) {
        List<RoomResponseDto> rooms = departmentService.getRoomsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Head Retrieval ==========

    @GetMapping("/head-doctor/{departmentId}")
    @Operation(summary = "Get head doctor of department")
    public ResponseEntity<ApiResponse> getDepartmentHeadDoctor(@PathVariable Long departmentId) {
        DoctorResponseDto doctor = departmentService.getDepartmentHeadDoctor(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head doctor retrieved successfully")
                .data(doctor)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/head-nurse/{departmentId}")
    @Operation(summary = "Get head nurse of department")
    public ResponseEntity<ApiResponse> getDepartmentHeadNurse(@PathVariable Long departmentId) {
        NurseResponseDto nurse = departmentService.getDepartmentHeadNurse(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Head nurse retrieved successfully")
                .data(nurse)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDto department = departmentService.getDepartmentById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department retrieved successfully")
                .data(department)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-code")
    @Operation(summary = "Get department by code")
    public ResponseEntity<ApiResponse> getDepartmentByCode(@RequestParam String code) {
        DepartmentResponseDto department = departmentService.getDepartmentByCode(code);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Department retrieved successfully")
                .data(department)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-code")
    @Operation(summary = "Get active department by code")
    public ResponseEntity<ApiResponse> getActiveDepartmentByCode(@RequestParam String code) {
        DepartmentResponseDto department = departmentService.getDepartmentByCodeAndIsActiveTrue(code);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active department retrieved successfully")
                .data(department)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get departments by name")
    public ResponseEntity<ApiResponse> getDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentByName(name);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-name")
    @Operation(summary = "Get active departments by name")
    public ResponseEntity<ApiResponse> getActiveDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentByNameAndIsActiveTru(name);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active departments")
    public ResponseEntity<ApiResponse> getActiveDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getActiveDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive departments")
    public ResponseEntity<ApiResponse> getInactiveDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getInactiveDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all departments")
    public ResponseEntity<ApiResponse> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Search ==========

    @GetMapping("/search/by-name")
    @Operation(summary = "Search departments by name (partial match, case-insensitive)")
    public ResponseEntity<ApiResponse> searchDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByName(name);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Departments found successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/by-location")
    @Operation(summary = "Search departments by location (partial match, case-insensitive)")
    public ResponseEntity<ApiResponse> searchDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByLocation(location);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Departments found successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-location")
    @Operation(summary = "Get departments by exact location")
    public ResponseEntity<ApiResponse> getDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByLocation(location);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Get departments by status")
    public ResponseEntity<ApiResponse> getDepartmentsByStatus(@RequestParam boolean isActive) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByStatus(isActive);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/active/by-name")
    @Operation(summary = "Search active departments by name")
    public ResponseEntity<ApiResponse> searchActiveDepartmentsByName(@RequestParam String name) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByNameAndIsActiveTrue(name);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments found successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/active/by-location")
    @Operation(summary = "Search active departments by location")
    public ResponseEntity<ApiResponse> searchActiveDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.searchDepartmentsByLocationAndIsActiveTrue(location);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments found successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-location")
    @Operation(summary = "Get active departments by exact location")
    public ResponseEntity<ApiResponse> getActiveDepartmentsByLocation(@RequestParam String location) {
        List<DepartmentResponseDto> departments = departmentService.getDepartmentsByLocationAndIsActiveTrue(location);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments retrieved successfully")
                .data(departments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========

    @GetMapping("/count/doctors")
    @Operation(summary = "Count doctors in a department")
    public ResponseEntity<ApiResponse> countDoctorsInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countDoctorsInDepartment(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/nurses")
    @Operation(summary = "Count nurses in a department")
    public ResponseEntity<ApiResponse> countNursesInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countNursesInDepartment(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Nurse count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/rooms")
    @Operation(summary = "Count rooms in a department")
    public ResponseEntity<ApiResponse> countRoomsInDepartment(@RequestParam Long departmentId) {
        Long count = departmentService.countRoomsInDepartment(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/total")
    @Operation(summary = "Count total departments")
    public ResponseEntity<ApiResponse> countTotalDepartments() {
        Long count = departmentService.countTotalDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Total departments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Count active departments")
    public ResponseEntity<ApiResponse> countActiveDepartments() {
        Long count = departmentService.countActiveDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active departments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/inactive")
    @Operation(summary = "Count inactive departments")
    public ResponseEntity<ApiResponse> countInactiveDepartments() {
        Long count = departmentService.countInactiveDepartments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive departments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}
