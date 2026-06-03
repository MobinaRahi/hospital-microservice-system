package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.service.DoctorScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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
    public ResponseEntity<ApiResponse> createDoctorSchedule(@Valid @RequestBody DoctorScheduleCreateDto doctorSchedule) {
        DoctorScheduleResponseDto created = doctorScheduleService.createDoctorSchedule(doctorSchedule);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Doctor schedule created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create doctor schedules")
    public ResponseEntity<ApiResponse> bulkCreateDoctorSchedules(@RequestBody List<DoctorScheduleCreateDto> doctorScheduleList) {
        doctorScheduleService.bulkCreateDoctorSchedules(doctorScheduleList);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Doctor schedules created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a doctor schedule by ID")
    public ResponseEntity<ApiResponse> updateDoctorSchedule(@PathVariable Long id, @Valid @RequestBody DoctorScheduleUpdateDto doctorSchedule) {
        DoctorScheduleResponseDto updated = doctorScheduleService.updateDoctorSchedule(id, doctorSchedule);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedule updated successfully with id: " + id)
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Status Management ==========

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a doctor schedule by ID")
    public ResponseEntity<ApiResponse> deactivateDoctorSchedule(@PathVariable Long id) {
        doctorScheduleService.deactivateDoctorSchedule(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedule deactivated successfully with id: " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a doctor schedule by ID")
    public ResponseEntity<ApiResponse> activateDoctorSchedule(@PathVariable Long id) {
        doctorScheduleService.activateDoctorSchedule(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedule activated successfully with id: " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/all")
    @Operation(summary = "Get all doctor schedules")
    public ResponseEntity<ApiResponse> getAllDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getAllDoctorSchedules();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All doctor schedules retrieved successfully")
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{doctorScheduleId}")
    @Operation(summary = "Get doctor schedule by ID")
    public ResponseEntity<ApiResponse> getDoctorScheduleById(@PathVariable Long doctorScheduleId) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedule retrieved successfully")
                .data(doctorSchedule)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Retrieval by Doctor ID ==========

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get all doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse> getDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByDoctorId(doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedules retrieved successfully for doctor id: " + doctorId)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/doctor/{doctorId}")
    @Operation(summary = "Get active doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse> getActiveDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByDoctorId(doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active doctor schedules retrieved successfully for doctor id: " + doctorId)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive/doctor/{doctorId}")
    @Operation(summary = "Get inactive doctor schedules by doctor ID")
    public ResponseEntity<ApiResponse> getInactiveDoctorSchedulesByDoctorId(@PathVariable Long doctorId) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getInactiveDoctorSchedulesByDoctorId(doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive doctor schedules retrieved successfully for doctor id: " + doctorId)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Retrieval by Doctor ID and Day ==========

    @GetMapping("/doctor/{doctorId}/day/{dayOfWeek}")
    @Operation(summary = "Get doctor schedule by doctor ID and day of week")
    public ResponseEntity<ApiResponse> getDoctorScheduleByDoctorAndDay(
            @PathVariable Long doctorId,
            @PathVariable DayOfWeek dayOfWeek) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getDoctorScheduleByDoctorAndDay(doctorId, dayOfWeek);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedule retrieved successfully for doctor: " + doctorId + " on " + dayOfWeek)
                .data(doctorSchedule)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/doctor/{doctorId}/day/{dayOfWeek}")
    @Operation(summary = "Get active doctor schedule by doctor ID and day of week")
    public ResponseEntity<ApiResponse> getActiveDoctorScheduleByDoctorAndDay(
            @PathVariable Long doctorId,
            @PathVariable DayOfWeek dayOfWeek) {
        DoctorScheduleResponseDto doctorSchedule = doctorScheduleService.getActiveDoctorScheduleByDoctorAndDay(doctorId, dayOfWeek);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active doctor schedule retrieved successfully for doctor: " + doctorId + " on " + dayOfWeek)
                .data(doctorSchedule)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Status-based Lists ==========

    @GetMapping("/active")
    @Operation(summary = "Get all active doctor schedules")
    public ResponseEntity<ApiResponse> getActiveDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedules();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active doctor schedules retrieved successfully")
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive doctor schedules")
    public ResponseEntity<ApiResponse> getInactiveDoctorSchedules() {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getInactiveDoctorSchedules();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive doctor schedules retrieved successfully")
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Time-based Queries ==========

    @GetMapping("/by-start-time-after")
    @Operation(summary = "Get doctor schedules by start time after")
    public ResponseEntity<ApiResponse> getDoctorSchedulesByStartTimeAfter(@RequestParam LocalTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByStartTimeAfter(time);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedules retrieved successfully by start time after: " + time)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-start-time-after")
    @Operation(summary = "Get active doctor schedules by start time after")
    public ResponseEntity<ApiResponse> getActiveDoctorSchedulesByStartTimeAfter(@RequestParam LocalTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByStartTimeAfter(time);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active doctor schedules retrieved successfully by start time after: " + time)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-end-time-before")
    @Operation(summary = "Get doctor schedules by end time before")
    public ResponseEntity<ApiResponse> getDoctorSchedulesByEndTimeBefore(@RequestParam LocalTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getDoctorSchedulesByEndTimeBefore(time);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Doctor schedules retrieved successfully by end time before: " + time)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-end-time-before")
    @Operation(summary = "Get active doctor schedules by end time before")
    public ResponseEntity<ApiResponse> getActiveDoctorSchedulesByEndTimeBefore(@RequestParam LocalTime time) {
        List<DoctorScheduleResponseDto> doctorScheduleList = doctorScheduleService.getActiveDoctorSchedulesByEndTimeBefore(time);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active doctor schedules retrieved successfully by end time before: " + time)
                .data(doctorScheduleList)
                .build();
        return ResponseEntity.ok(response);
    }
}