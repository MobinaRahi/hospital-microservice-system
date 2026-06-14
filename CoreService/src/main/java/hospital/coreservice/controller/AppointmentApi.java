package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * REST controller for Appointment management.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "Appointment CRUD and management APIs")
public class AppointmentApi {

    private final AppointmentService appointmentService;

    // ========== Core Operations (Create, Update, Cancel, Check-in, Complete) ==========

    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<ApiResponse> createAppointment(@Valid @RequestBody AppointmentCreateDto createDto) {
        AppointmentResponseDto created = appointmentService.createAppointment(createDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Appointment created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an appointment by ID")
    public ResponseEntity<ApiResponse> updateAppointmentById(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateDto updateDto) {
        AppointmentResponseDto updated = appointmentService.updateAppointment(id, updateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment updated successfully with id: " + id)
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment by ID")
    public ResponseEntity<ApiResponse> cancelAppointmentById(
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestParam Long canceledBy) {
        appointmentService.cancelAppointment(id, reason, canceledBy);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment cancelled successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/check-in")
    @Operation(summary = "Check-in an appointment by ID")
    public ResponseEntity<ApiResponse> checkInAppointment(@PathVariable Long id) {
        appointmentService.checkInAppointment(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment checked in successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete an appointment by ID")
    public ResponseEntity<ApiResponse> completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment completed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Reschedule Appointment ==========

    @PutMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an appointment")
    public ResponseEntity<ApiResponse> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam LocalDate newDate,
            @RequestParam LocalTime newStartTime,
            @RequestParam LocalTime newEndTime) {
        AppointmentResponseDto updated = appointmentService.rescheduleAppointment(id, newDate, newStartTime, newEndTime);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment rescheduled successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by ID")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointment found successfully")
                .data(appointment)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient ID")
    public ResponseEntity<ApiResponse> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments by doctor ID")
    public ResponseEntity<ApiResponse> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get appointments by department ID")
    public ResponseEntity<ApiResponse> getAppointmentsByDepartmentId(@PathVariable Long departmentId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Filter by Status / Date / Range ==========

    @GetMapping("/by-status")
    @Operation(summary = "Get appointments by status")
    public ResponseEntity<ApiResponse> getAppointmentsByStatus(@RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByStatus(status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get appointments by date")
    public ResponseEntity<ApiResponse> getAppointmentsByDate(@RequestParam LocalDate date) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDate(date);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date-range")
    @Operation(summary = "Get appointments by date range")
    public ResponseEntity<ApiResponse> getAppointmentsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Time-based Queries (Today, Week, Month, Created/Cancelled After/Before) ==========

    @GetMapping("/time/today")
    @Operation(summary = "Get today's appointments")
    public ResponseEntity<ApiResponse> getTodayAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getTodayAppointments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/this-week")
    @Operation(summary = "Get this week's appointments")
    public ResponseEntity<ApiResponse> getThisWeekAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getThisWeekAppointments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/this-month")
    @Operation(summary = "Get this month's appointments")
    public ResponseEntity<ApiResponse> getThisMonthAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getThisMonthAppointments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/created-after")
    @Operation(summary = "Get appointments created after a specific timestamp")
    public ResponseEntity<ApiResponse> getAppointmentsByCreatedAtAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAtAfter) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedAtAfter(createdAtAfter);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/created-before")
    @Operation(summary = "Get appointments created before a specific timestamp")
    public ResponseEntity<ApiResponse> getAppointmentsByCreatedAtBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAtBefore) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedAtBefore(createdAtBefore);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/canceled-after")
    @Operation(summary = "Get appointments canceled after a specific timestamp")
    public ResponseEntity<ApiResponse> getAppointmentsByCanceledAtAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime canceledAfter) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledAtAfter(canceledAfter);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/time/canceled-before")
    @Operation(summary = "Get appointments canceled before a specific timestamp")
    public ResponseEntity<ApiResponse> getAppointmentsByCanceledAtBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime canceledBefore) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledAtBefore(canceledBefore);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Patient-specific Queries (Upcoming, Past, By Status, Count) ==========

    @GetMapping("/patient/{patientId}/upcoming")
    @Operation(summary = "Get upcoming appointments for a patient")
    public ResponseEntity<ApiResponse> getUpcomingAppointments(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getUpcomingAppointments(patientId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}/past")
    @Operation(summary = "Get past appointments for a patient")
    public ResponseEntity<ApiResponse> getPastAppointments(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getPastAppointments(patientId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}/by-status")
    @Operation(summary = "Get appointments for a patient by status")
    public ResponseEntity<ApiResponse> getAppointmentsByPatientAndStatus(
            @PathVariable Long patientId,
            @RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByPatientAndStatus(patientId, status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/count/{patientId}")
    @Operation(summary = "Count appointments for a patient by status")
    public ResponseEntity<ApiResponse> countAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam AppointmentStatus status) {
        Long count = appointmentService.countAppointmentsByPatientAndStatus(patientId, status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Doctor-specific Queries (By Status, Count, Available Slots) ==========

    @GetMapping("/doctor/{doctorId}/by-status")
    @Operation(summary = "Get appointments for a doctor by status")
    public ResponseEntity<ApiResponse> getAppointmentsByDoctorAndStatus(
            @PathVariable Long doctorId,
            @RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDoctorAndStatus(doctorId, status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/count/{doctorId}")
    @Operation(summary = "Count appointments for a doctor by status")
    public ResponseEntity<ApiResponse> countAppointmentsByDoctorAndStatus(
            @PathVariable Long doctorId,
            @RequestParam AppointmentStatus status) {
        Long count = appointmentService.countAppointmentsByDoctorAndStatus(doctorId, status);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/available")
    @Operation(summary = "Get available time slots for a doctor on a specific date")
    public ResponseEntity<ApiResponse> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date) {
        List<LocalTime> available = appointmentService.getAvailableSlots(doctorId, date);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Available slots retrieved successfully")
                .data(available)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Special Cases ==========

    @GetMapping("/no-show")
    @Operation(summary = "Get no-show appointments")
    public ResponseEntity<ApiResponse> getNoShowAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getNoShowAppointments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total appointments")
    public ResponseEntity<ApiResponse> countTotalAppointments() {
        Long count = appointmentService.countTotalAppointments();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Audit / User Tracking ==========

    @GetMapping("/created-by/{createdBy}")
    @Operation(summary = "Get appointments created by a specific user")
    public ResponseEntity<ApiResponse> getAppointmentsByCreatedBy(@PathVariable Long createdBy) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedBy(createdBy);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/canceled-by/{canceledBy}")
    @Operation(summary = "Get appointments canceled by a specific user")
    public ResponseEntity<ApiResponse> getAppointmentsByCanceledBy(@PathVariable Long canceledBy) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledBy(canceledBy);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Appointments found successfully")
                .data(appointments)
                .build();
        return ResponseEntity.ok(response);
    }
}


