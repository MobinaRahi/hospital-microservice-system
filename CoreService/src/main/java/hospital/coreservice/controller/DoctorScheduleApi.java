package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.service.DoctorScheduleService;
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
@RequestMapping("/api/v1/doctor-schedules")
@RequiredArgsConstructor
@Tag(name = "DoctorSchedule Management", description = "DoctorSchedule CRUD and management APIs")
public class DoctorScheduleApi {

    private final DoctorScheduleService doctorScheduleService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new doctor schedule")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> createDoctorSchedule(@Valid @RequestBody DoctorScheduleCreateDto doctorSchedule) {
        DoctorScheduleResponseDto created = doctorScheduleService.createDoctorSchedule(doctorSchedule);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Doctor schedule created successfully", HttpStatus.CREATED.value()));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create doctor schedules")
    public ResponseEntity<ApiResponse<Void>> bulkCreateDoctorSchedules(@RequestBody List<DoctorScheduleCreateDto> doctorScheduleList) {
        doctorScheduleService.bulkCreateDoctorSchedules(doctorScheduleList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor schedules created successfully", HttpStatus.CREATED.value()));
    }

    @PatchMapping("/by-doctor-schedule-id/{id}")
    @Operation(summary = "Update a doctor schedule by ID")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> updateDoctorSchedule(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleUpdateDto doctorSchedule) {
        DoctorScheduleResponseDto updated = doctorScheduleService.updateDoctorSchedule(id, doctorSchedule);
        return ResponseEntity.ok(ApiResponse.success(updated, "Doctor schedule updated successfully with id: " + id, HttpStatus.OK.value()));
    }

    // ========== Status Management ==========

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a doctor schedule by ID")
    public ResponseEntity<ApiResponse<Void>> deactivateDoctorSchedule(@PathVariable Long id) {
        doctorScheduleService.deactivateDoctorSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor schedule deactivated successfully with id: " + id, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a doctor schedule by ID")
    public ResponseEntity<ApiResponse<Void>> activateDoctorSchedule(@PathVariable Long id) {
        doctorScheduleService.activateDoctorSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor schedule activated successfully with id: " + id, HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/all")
    @Operation(summary = "Get all doctor schedules")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getAllDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getAllDoctorSchedules();
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "All doctor schedules retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{doctorScheduleId}")
    @Operation(summary = "Get doctor schedule by ID")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorScheduleById(@PathVariable Long doctorScheduleId) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);
        return ResponseEntity.ok(ApiResponse.success(doctorSchedule, "Doctor schedule retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Retrieval by Doctor ID ==========

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get all doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Doctor schedules retrieved successfully for doctor id: " + doctorId, HttpStatus.OK.value()));
    }

    @GetMapping("/active/doctor/{doctorId}")
    @Operation(summary = "Get active doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getActiveDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Active doctor schedules retrieved successfully for doctor id: " + doctorId, HttpStatus.OK.value()));
    }

    @GetMapping("/inactive/doctor/{doctorId}")
    @Operation(summary = "Get inactive doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getInactiveDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getInactiveDoctorSchedulesByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Inactive doctor schedules retrieved successfully for doctor id: " + doctorId, HttpStatus.OK.value()));
    }

    // ========== Retrieval by Doctor ID and Day ==========

    @GetMapping("/doctor/{doctorId}/day/{dayOfWeek}")
    @Operation(summary = "Get doctor schedule by doctor ID and day of week")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorScheduleByDoctorAndDay(
            @PathVariable Long doctorId,
            @PathVariable DayOfWeek dayOfWeek) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getDoctorScheduleByDoctorAndDay(doctorId, dayOfWeek);
        return ResponseEntity.ok(ApiResponse.success(doctorSchedule, "Doctor schedule retrieved successfully for doctor: " + doctorId + " on " + dayOfWeek, HttpStatus.OK.value()));
    }

    @GetMapping("/active/doctor/{doctorId}/day/{dayOfWeek}")
    @Operation(summary = "Get active doctor schedule by doctor ID and day of week")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getActiveDoctorScheduleByDoctorAndDay(
            @PathVariable Long doctorId,
            @PathVariable DayOfWeek dayOfWeek) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getActiveDoctorScheduleByDoctorAndDay(doctorId, dayOfWeek);
        return ResponseEntity.ok(ApiResponse.success(doctorSchedule, "Active doctor schedule retrieved successfully for doctor: " + doctorId + " on " + dayOfWeek, HttpStatus.OK.value()));
    }

    // ========== Status-based Lists ==========

    @GetMapping("/active")
    @Operation(summary = "Get all active doctor schedules")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getActiveDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedules();
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Active doctor schedules retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive doctor schedules")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getInactiveDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getInactiveDoctorSchedules();
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Inactive doctor schedules retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Time-based Queries (با LocalDateTime) ==========

    @GetMapping("/by-start-time-after")
    @Operation(summary = "Get doctor schedules by start time after")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getDoctorSchedulesByStartTimeAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByStartTimeAfter(time);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Doctor schedules retrieved successfully by start time after: " + time, HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-start-time-after")
    @Operation(summary = "Get active doctor schedules by start time after")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getActiveDoctorSchedulesByStartTimeAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByStartTimeAfter(time);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Active doctor schedules retrieved successfully by start time after: " + time, HttpStatus.OK.value()));
    }

    @GetMapping("/by-end-time-before")
    @Operation(summary = "Get doctor schedules by end time before")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getDoctorSchedulesByEndTimeBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByEndTimeBefore(time);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Doctor schedules retrieved successfully by end time before: " + time, HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-end-time-before")
    @Operation(summary = "Get active doctor schedules by end time before")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDto>>> getActiveDoctorSchedulesByEndTimeBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByEndTimeBefore(time);
        return ResponseEntity.ok(ApiResponse.success(doctorScheduleList, "Active doctor schedules retrieved successfully by end time before: " + time, HttpStatus.OK.value()));
    }
}