package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
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
    @Operation(summary = " Create a new doctor")
    public ResponseEntity<ApiResponse> createDoctor(@Valid @RequestParam DoctorCreateDto doctorCreateDto) {
        DoctorResponseDto created = doctorService.createDoctor(doctorCreateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Doctor created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor By Id")
    public ResponseEntity<ApiResponse> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDto doctorUpdateDto) {
        DoctorResponseDto updated = doctorService.updateDoctor(id, doctorUpdateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("deactivate/{id}")
    @Operation(summary = "deactivate a doctor By Id")
    public ResponseEntity<ApiResponse> deactivateDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("deactivate Doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/add/sub-speciality/{doctorId}")
    @Operation(summary = "add sub speciality to doctor")
    public ResponseEntity<ApiResponse> addSubSpeciality(@RequestBody SubSpeciality subSpeciality, @PathVariable Long doctorId) {
        doctorService.addSubSpeciality(doctorId, subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("add sub speciality to doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/remove/sub-speciality/{doctorId}")
    @Operation(summary = "remove sub speciality from doctor")
    public ResponseEntity<ApiResponse> removeSubSpeciality(@RequestBody SubSpeciality subSpeciality, @PathVariable Long doctorId) {
        doctorService.removeSubSpeciality(doctorId, subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("remove sub speciality from doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/add/department/{doctorId}")
    @Operation(summary = "add department to doctor")
    public ResponseEntity<ApiResponse> assignDepartment(@RequestBody Long departmentId, @PathVariable Long doctorId) {
        doctorService.assignDepartment(doctorId, departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("add department to doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/remove/department/{doctorId}")
    @Operation(summary = "remove department from doctor")
    public ResponseEntity<ApiResponse> removeDepartment(@PathVariable Long doctorId) {
        doctorService.removeDepartment(doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("remove sub department from doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PatchMapping("active/{id}")
    @Operation(summary = "active a doctor By Id")
    public ResponseEntity<ApiResponse> activateDoctor(@PathVariable Long id) {
        doctorService.activateDoctor(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("active Doctor successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{id}")
    @Operation(summary = "get a doctor By Id")
    public ResponseEntity<ApiResponse> getDoctorById(@PathVariable Long id) {
        DoctorResponseDto doctorDto = doctorService.getDoctorById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor successfully")
                .data(doctorDto)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-user-id")
    @Operation(summary = "get a doctor By user ID")
    public ResponseEntity<ApiResponse> getDoctorByUserId(@RequestParam Long userId) {
        DoctorResponseDto doctorDto = doctorService.getDoctorByUserId(userId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By user ID successfully")
                .data(doctorDto)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-license-number")
    @Operation(summary = "get a doctor By license Number")
    public ResponseEntity<ApiResponse> getDoctorByLicenseNumber(@RequestParam String licenseNumber) {
        DoctorResponseDto doctorDto = doctorService.getDoctorByLicenseNumber(licenseNumber);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By license Number successfully")
                .data(doctorDto)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-speciality")
    @Operation(summary = "get a doctor By speciality")
    public ResponseEntity<ApiResponse> getDoctorsBySpeciality(@RequestParam Speciality speciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySpeciality(speciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By speciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-sub-speciality")
    @Operation(summary = "get a doctor By subSpeciality")
    public ResponseEntity<ApiResponse> getDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySubSpeciality(subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By subSpeciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-department-ID")
    @Operation(summary = "get a doctor By departmentId")
    public ResponseEntity<ApiResponse> getDoctorsByDepartmentId(@RequestParam Long departmentId) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By departmentId successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-experience-range")
    @Operation(summary = "get a doctor By ExperienceRange")
    public ResponseEntity<ApiResponse> getDoctorsByExperienceRange(@RequestParam int minYears, @RequestParam int maxYears) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsByExperienceRange(minYears, maxYears);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By ExperienceRange successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/by-speciality-and-sub-speciality")
    @Operation(summary = "get a doctor By Speciality And SubSpeciality")
    public ResponseEntity<ApiResponse> getDoctorsBySpecialityAndSubSpeciality(@RequestParam Speciality speciality, @RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getDoctorsBySpecialityAndSubSpeciality(speciality, subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get doctor By By Speciality And SubSpeciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    @Operation(summary = "get all doctors")
    public ResponseEntity<ApiResponse> getAllDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getAllDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get all doctors successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/by-name")
    @Operation(summary = "search doctors by name")
    public ResponseEntity<ApiResponse> searchDoctorsByName(@RequestParam String name, @RequestParam String lastName) {
        List<DoctorResponseDto> doctorDtoList = doctorService.searchDoctorsByName(name, lastName);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("search doctors by name successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/available/by-day")
    @Operation(summary = "get Available Doctors By Day")
    public ResponseEntity<ApiResponse> getAvailableDoctorsByDay(@RequestParam DayOfWeek dayOfWeek) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getAvailableDoctorsByDay(dayOfWeek);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get Available Doctors By Day successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active")
    @Operation(summary = "get all active doctors")
    public ResponseEntity<ApiResponse> getActiveDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get all active doctors successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/inActive")
    @Operation(summary = "get all inActive doctors")
    public ResponseEntity<ApiResponse> getInactiveDoctors() {
        List<DoctorResponseDto> doctorDtoList = doctorService.getInactiveDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get all inActive doctors successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active/by-speciality")
    @Operation(summary = "get active doctors By speciality")
    public ResponseEntity<ApiResponse> getActiveDoctorsBySpeciality(@RequestParam Speciality speciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySpeciality(speciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get active doctors By speciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active/by-sub-speciality")
    @Operation(summary = "get active doctors By subSpeciality")
    public ResponseEntity<ApiResponse> getActiveDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySubSpeciality(subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get active doctors By subSpeciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active/by-department-ID")
    @Operation(summary = "get active doctors By departmentId")
    public ResponseEntity<ApiResponse> getActiveDoctorsByDepartmentId(@RequestParam Long departmentId) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get active doctors By departmentId successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active/by-speciality-and-sub-speciality")
    @Operation(summary = "get active doctors By Speciality And SubSpeciality")
    public ResponseEntity<ApiResponse> getActiveDoctorsBySpecialityAndSubSpeciality(@RequestParam Speciality speciality, @RequestParam SubSpeciality subSpeciality) {
        List<DoctorResponseDto> doctorDtoList = doctorService.getActiveDoctorsBySpecialityAndSubSpeciality(speciality, subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("get active doctors By By Speciality And SubSpeciality successfully")
                .data(doctorDtoList)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/count")
    @Operation(summary = "count doctors ")
    public ResponseEntity<ApiResponse> countAllDoctors() {
        Long count = doctorService.countAllDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count doctors successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/active/count")
    @Operation(summary = "count Active doctors ")
    public ResponseEntity<ApiResponse> countActiveDoctors() {
        Long count = doctorService.countActiveDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count Active doctors successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/inActive/count")
    @Operation(summary = "count inActive doctors ")
    public ResponseEntity<ApiResponse> countInactiveDoctors() {
        Long count = doctorService.countInactiveDoctors();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count inActive doctors successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/count/by/speciality")
    @Operation(summary = "count doctors by speciality")
    public ResponseEntity<ApiResponse> countDoctorsBySpeciality(@RequestParam Speciality speciality) {
        Long count = doctorService.countDoctorsBySpeciality(speciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count doctors by speciality successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/count/by/sub-speciality")
    @Operation(summary = "count doctors by sub speciality")
    public ResponseEntity<ApiResponse> countDoctorsBySubSpeciality(@RequestParam SubSpeciality subSpeciality) {
        Long count = doctorService.countDoctorsBySubSpeciality(subSpeciality);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count doctors by sub speciality successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/count/by/department-id")
    @Operation(summary = "count doctors by departmentId")
    public ResponseEntity<ApiResponse> countDoctorsByDepartmentId(@RequestParam Long departmentId) {
        Long count = doctorService.countDoctorsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("count doctors by departmentId successfully")
                .data(count)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
