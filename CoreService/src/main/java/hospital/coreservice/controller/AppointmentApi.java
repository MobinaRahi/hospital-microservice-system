package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> createAppointment(@Valid @RequestBody AppointmentCreateDto createDto) {
        AppointmentResponseDto created = appointmentService.createAppointment(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Appointment created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an appointment by ID")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointmentById(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateDto updateDto) {
        AppointmentResponseDto updated = appointmentService.updateAppointment(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Appointment updated successfully with id: " + id, HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment by ID")
    public ResponseEntity<ApiResponse<Void>> cancelAppointmentById(
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestParam Long canceledBy) {
        appointmentService.cancelAppointment(id, reason, canceledBy);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/check-in")
    @Operation(summary = "Check-in an appointment by ID")
    public ResponseEntity<ApiResponse<Void>> checkInAppointment(@PathVariable Long id) {
        appointmentService.checkInAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment checked in successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete an appointment by ID")
    public ResponseEntity<ApiResponse<Void>> completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment completed successfully", HttpStatus.OK.value()));
    }

    // ========== Reschedule Appointment ==========

    @PutMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an appointment")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam LocalDate newDate,
            @RequestParam LocalTime newStartTime,
            @RequestParam LocalTime newEndTime) {
        AppointmentResponseDto updated = appointmentService.rescheduleAppointment(id, newDate, newStartTime, newEndTime);
        return ResponseEntity.ok(ApiResponse.success(updated, "Appointment rescheduled successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by ID")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(appointment, "Appointment found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient ID")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments by doctor ID")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get appointments by department ID")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByDepartmentId(@PathVariable Long departmentId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    // ========== Filter by Status / Date / Range ==========

    @GetMapping("/by-status")
    @Operation(summary = "Get appointments by status")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByStatus(@RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get appointments by date")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByDate(@RequestParam LocalDate date) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-date-range")
    @Operation(summary = "Get appointments by date range")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    // ========== Time-based Queries (Today, Week, Month, Created/Cancelled After/Before) ==========

    @GetMapping("/time/today")
    @Operation(summary = "Get today's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getTodayAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getTodayAppointments();
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/this-week")
    @Operation(summary = "Get this week's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getThisWeekAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getThisWeekAppointments();
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/this-month")
    @Operation(summary = "Get this month's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getThisMonthAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getThisMonthAppointments();
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/created-after")
    @Operation(summary = "Get appointments created after a specific timestamp")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCreatedAtAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAtAfter) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedAtAfter(createdAtAfter);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/created-before")
    @Operation(summary = "Get appointments created before a specific timestamp")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCreatedAtBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAtBefore) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedAtBefore(createdAtBefore);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/canceled-after")
    @Operation(summary = "Get appointments canceled after a specific timestamp")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCanceledAtAfter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime canceledAfter) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledAtAfter(canceledAfter);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/time/canceled-before")
    @Operation(summary = "Get appointments canceled before a specific timestamp")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCanceledAtBefore(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime canceledBefore) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledAtBefore(canceledBefore);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    // ========== Patient-specific Queries (Upcoming, Past, By Status, Count) ==========

    @GetMapping("/patient/{patientId}/upcoming")
    @Operation(summary = "Get upcoming appointments for a patient")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getUpcomingAppointments(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getUpcomingAppointments(patientId);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/past")
    @Operation(summary = "Get past appointments for a patient")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getPastAppointments(@PathVariable Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getPastAppointments(patientId);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/by-status")
    @Operation(summary = "Get appointments for a patient by status")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByPatientAndStatus(
            @PathVariable Long patientId,
            @RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByPatientAndStatus(patientId, status);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/count/{patientId}")
    @Operation(summary = "Count appointments for a patient by status")
    public ResponseEntity<ApiResponse<Long>> countAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam AppointmentStatus status) {
        Long count = appointmentService.countAppointmentsByPatientAndStatus(patientId, status);
        return ResponseEntity.ok(ApiResponse.success(count, "Appointments count retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Doctor-specific Queries (By Status, Count, Available Slots) ==========

    @GetMapping("/doctor/{doctorId}/by-status")
    @Operation(summary = "Get appointments for a doctor by status")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByDoctorAndStatus(
            @PathVariable Long doctorId,
            @RequestParam AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDoctorAndStatus(doctorId, status);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/count/{doctorId}")
    @Operation(summary = "Count appointments for a doctor by status")
    public ResponseEntity<ApiResponse<Long>> countAppointmentsByDoctorAndStatus(
            @PathVariable Long doctorId,
            @RequestParam AppointmentStatus status) {
        Long count = appointmentService.countAppointmentsByDoctorAndStatus(doctorId, status);
        return ResponseEntity.ok(ApiResponse.success(count, "Appointments count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/doctor/available")
    @Operation(summary = "Get available time slots for a doctor on a specific date")
    public ResponseEntity<ApiResponse<List<LocalTime>>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date) {
        List<LocalTime> available = appointmentService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(ApiResponse.success(available, "Available slots retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Special Cases ==========

    @GetMapping("/no-show")
    @Operation(summary = "Get no-show appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getNoShowAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getNoShowAppointments();
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total appointments")
    public ResponseEntity<ApiResponse<Long>> countTotalAppointments() {
        Long count = appointmentService.countTotalAppointments();
        return ResponseEntity.ok(ApiResponse.success(count, "Appointments count retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Audit / User Tracking ==========

    @GetMapping("/created-by/{createdBy}")
    @Operation(summary = "Get appointments created by a specific user")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCreatedBy(@PathVariable Long createdBy) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCreatedBy(createdBy);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/canceled-by/{canceledBy}")
    @Operation(summary = "Get appointments canceled by a specific user")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointmentsByCanceledBy(@PathVariable Long canceledBy) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByCanceledBy(canceledBy);
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found successfully", HttpStatus.OK.value()));
    }
}