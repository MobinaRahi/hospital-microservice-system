package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.doctor.DoctorUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import hospital.coreservice.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "Doctor CRUD and management APIs")
public class DoctorApi {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Create a new doctor")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> createDoctor(@Valid @RequestBody DoctorCreateDto doctorCreateDto) {
        DoctorResponseDto created = doctorService.createDoctor(doctorCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Doctor created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor by ID")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDto doctorUpdateDto) {
        DoctorResponseDto updated = doctorService.updateDoctor(id, doctorUpdateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Doctor updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate a doctor by ID")
    public ResponseEntity<ApiResponse<Void>> deactivateDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Deactivate Doctor successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/add/sub-speciality/{doctorId}")
    @Operation(summary = "Add sub-speciality to doctor")
    public ResponseEntity<ApiResponse<Void>> addSubSpeciality(@RequestBody SubSpeciality subSpeciality, @PathVariable Long doctorId) {
        doctorService.addSubSpeciality(doctorId, subSpeciality);
        return ResponseEntity.ok(ApiResponse.success("Add sub-speciality to doctor successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/remove/sub-speciality/{doctorId}")
    @Operation(summary = "Remove sub-speciality from doctor")
    public ResponseEntity<ApiResponse<Void>> removeSubSpeciality(@RequestBody SubSpeciality subSpeciality, @PathVariable Long doctorId) {
        doctorService.removeSubSpeciality(doctorId, subSpeciality);
        return ResponseEntity.ok(ApiResponse.success("Remove sub-speciality from doctor successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/add/department/{doctorId}")
    @Operation(summary = "Assign department to doctor")
    public ResponseEntity<ApiResponse<Void>> assignDepartment(@RequestBody Long departmentId, @PathVariable Long doctorId) {
        doctorService.assignDepartment(doctorId, departmentId);
        return ResponseEntity.ok(ApiResponse.success("Assign department to doctor successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/remove/department/{doctorId}")
    @Operation(summary = "Remove department from doctor")
    public ResponseEntity<ApiResponse<Void>> removeDepartment(@PathVariable Long doctorId) {
        doctorService.removeDepartment(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Remove department from doctor successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a doctor by ID")
    public ResponseEntity<ApiResponse<Void>> activateDoctor(@PathVariable Long id) {
        doctorService.activateDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Activate Doctor successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a doctor by ID")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDoctorById(@PathVariable Long id) {
        DoctorResponseDto doctorDto = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success(doctorDto, "Get doctor successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-user-id")
    @Operation(summary = "Get a doctor by user ID")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDoctorByUserId(@RequestParam Long userId) {
        DoctorResponseDto doctorDto = doctorService.getDoctorByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(doctorDto, "Get doctor by user ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-license-number")
    @Operation(summary = "Get a doctor by license number")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDoctorByLicenseNumber(@RequestParam String licenseNumber) {
        DoctorResponseDto doctorDto = doctorService.getDoctorByLicenseNumber(licenseNumber);
        return ResponseEntity.ok(ApiResponse.success(doctorDto, "Get doctor by license number successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-speciality")
    @Operation(summary = "Get doctors by speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsBySpeciality(@RequestParam Speciality speciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySpeciality(speciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get doctors by speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-sub-speciality")
    @Operation(summary = "Get doctors by sub-speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySubSpeciality(subSpeciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get doctors by sub-speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-department-id")
    @Operation(summary = "Get doctors by department ID")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsByDepartmentId(@RequestParam Long departmentId) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get doctors by department ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-experience-range")
    @Operation(summary = "Get doctors by experience range")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsByExperienceRange(@RequestParam int minYears, @RequestParam int maxYears) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsByExperienceRange(minYears, maxYears);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get doctors by experience range successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-speciality-and-sub-speciality")
    @Operation(summary = "Get doctors by speciality and sub-speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getDoctorsBySpecialityAndSubSpeciality(
            @RequestParam Speciality speciality,
            @RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySpecialityAndSubSpeciality(speciality, subSpeciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get doctors by speciality and sub-speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getAllDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get all doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search/by-name")
    @Operation(summary = "Search doctors by name")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> searchDoctorsByName(@RequestParam String name, @RequestParam String lastName) {
        List<DoctorResponseDto> doctorDtoList = doctorService.searchDoctorsByName(name, lastName);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Search doctors by name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/available/by-day")
    @Operation(summary = "Get available doctors by day")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getAvailableDoctorsByDay(@RequestParam DayOfWeek dayOfWeek) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getAvailableDoctorsByDay(dayOfWeek);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get available doctors by day successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getActiveDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get all active doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inActive")
    @Operation(summary = "Get all inactive doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getInactiveDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getInactiveDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get all inactive doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-speciality")
    @Operation(summary = "Get active doctors by speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getActiveDoctorsBySpeciality(@RequestParam Speciality speciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySpeciality(speciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get active doctors by speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-sub-speciality")
    @Operation(summary = "Get active doctors by sub-speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getActiveDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySubSpeciality(subSpeciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get active doctors by sub-speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-department-id")
    @Operation(summary = "Get active doctors by department ID")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getActiveDoctorsByDepartmentId(@RequestParam Long departmentId) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get active doctors by department ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-speciality-and-sub-speciality")
    @Operation(summary = "Get active doctors by speciality and sub-speciality")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getActiveDoctorsBySpecialityAndSubSpeciality(
            @RequestParam Speciality speciality,
            @RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySpecialityAndSubSpeciality(speciality, subSpeciality);
        return ResponseEntity.ok(ApiResponse.success(doctorDtoList, "Get active doctors by speciality and sub-speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count")
    @Operation(summary = "Count all doctors")
    public ResponseEntity<ApiResponse<Long>> countAllDoctors() {
        Long count = doctorService.countAllDoctors();
        return ResponseEntity.ok(ApiResponse.success(count, "Count doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active doctors")
    public ResponseEntity<ApiResponse<Long>> countActiveDoctors() {
        Long count = doctorService.countActiveDoctors();
        return ResponseEntity.ok(ApiResponse.success(count, "Count active doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inActive/count")
    @Operation(summary = "Count inactive doctors")
    public ResponseEntity<ApiResponse<Long>> countInactiveDoctors() {
        Long count = doctorService.countInactiveDoctors();
        return ResponseEntity.ok(ApiResponse.success(count, "Count inactive doctors successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-speciality")
    @Operation(summary = "Count doctors by speciality")
    public ResponseEntity<ApiResponse<Long>> countDoctorsBySpeciality(@RequestParam Speciality speciality) {
        Long count = doctorService.countDoctorsBySpeciality(speciality);
        return ResponseEntity.ok(ApiResponse.success(count, "Count doctors by speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-sub-speciality")
    @Operation(summary = "Count doctors by sub-speciality")
    public ResponseEntity<ApiResponse<Long>> countDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        Long count = doctorService.countDoctorsBySubSpeciality(subSpeciality);
        return ResponseEntity.ok(ApiResponse.success(count, "Count doctors by sub-speciality successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-department-id")
    @Operation(summary = "Count doctors by department ID")
    public ResponseEntity<ApiResponse<Long>> countDoctorsByDepartmentId(@RequestParam Long departmentId) {
        Long count = doctorService.countDoctorsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Count doctors by department ID successfully", HttpStatus.OK.value()));
    }
}