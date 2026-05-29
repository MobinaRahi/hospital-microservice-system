package hospital.coreservice.service;

import com.hospital.coreService.dto.appointment.AppointmentCreateDto;
import com.hospital.coreService.dto.appointment.AppointmentResponseDto;
import com.hospital.coreService.dto.appointment.AppointmentUpdateDto;
import com.hospital.coreService.model.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for Appointment management.
 *
 * @author Mobina
 */
public interface AppointmentService {

    // ========== Core Operations ==========

    /** Create new appointment */
    AppointmentResponseDto createAppointment(AppointmentCreateDto createDto);

    /** Update existing appointment */
    AppointmentResponseDto updateAppointment(Long id, AppointmentUpdateDto updateDto);

    /** Cancel appointment with reason */
    void cancelAppointment(Long id, String reason, Long canceledBy);

    /** Mark patient as arrived (check-in) */
    void checkInAppointment(Long id);

    /** Mark appointment as completed by doctor */
    void completeAppointment(Long id);

    // ========== Basic Retrieval ==========

    /** Get appointment by ID */
    AppointmentResponseDto getAppointmentById(Long id);

    /** Get all appointments for a patient */
    List<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId);

    /** Get all appointments for a doctor */
    List<AppointmentResponseDto> getAppointmentsByDoctorId(Long doctorId);

    /** Get appointments by department */
    List<AppointmentResponseDto> getAppointmentsByDepartmentId(Long departmentId);

    /** Get appointments by status */
    List<AppointmentResponseDto> getAppointmentsByStatus(AppointmentStatus status);

    // ========== Date Based Retrieval ==========

    /** Get appointments by exact date */
    List<AppointmentResponseDto> getAppointmentsByDate(LocalDate date);

    /** Get appointments between two dates */
    List<AppointmentResponseDto> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);

    /** Get today's appointments */
    List<AppointmentResponseDto> getTodayAppointments();

    /** Get this week's appointments (today to Friday) */
    List<AppointmentResponseDto> getThisWeekAppointments();

    /** Get this month's appointments */
    List<AppointmentResponseDto> getThisMonthAppointments();

    // ========== Patient Specific ==========

    /** Get upcoming appointments for a patient (today and future) */
    List<AppointmentResponseDto> getUpcomingAppointments(Long patientId);

    /** Get past appointments for a patient */
    List<AppointmentResponseDto> getPastAppointments(Long patientId);

    /** Get appointments by patient and status */
    List<AppointmentResponseDto> getAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status);

    /** Count appointments for a patient by status */
    Long countAppointmentsByPatient(Long patientId, AppointmentStatus status);

    // ========== Doctor Specific ==========

    /** Get appointments by doctor and status */
    List<AppointmentResponseDto> getAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status);

    /** Count appointments for a doctor by status */
    Long countAppointmentsByDoctor(Long doctorId, AppointmentStatus status);

    // ========== Audit / User Tracking ==========

    /** Get appointments created after a specific time */
    List<AppointmentResponseDto> getAppointmentsByCreatedAtAfter(LocalDateTime createdAt);

    /** Get appointments created before a specific time */
    List<AppointmentResponseDto> getAppointmentsByCreatedAtBefore(LocalDateTime createdAt);

    /** Get appointments cancelled after a specific time */
    List<AppointmentResponseDto> getAppointmentsByCanceledAtAfter(LocalDateTime canceledAt);

    /** Get appointments cancelled before a specific time */
    List<AppointmentResponseDto> getAppointmentsByCanceledAtBefore(LocalDateTime canceledAt);

    /** Get appointments created by a specific user */
    List<AppointmentResponseDto> getAppointmentsByCreatedBy(Long createdBy);

    /** Get appointments cancelled by a specific user */
    List<AppointmentResponseDto> getAppointmentsByCanceledBy(Long canceledBy);

    // ========== Special Cases ==========

    /** Get no-show appointments (patient didn't show up) */
    List<AppointmentResponseDto> getNoShowAppointments();

    // ========== Statistics ==========

    /** Get total appointments count */
    Long countTotalAppointments();

    // ========== Features ==========

    /** Get available time slots for a doctor on a specific date */
    List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date);

    /** Reschedule appointment to new date/time */
    AppointmentResponseDto rescheduleAppointment(Long id, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime);

    // ========== Validation ==========

    /** Check if doctor is available at given time */
    boolean isDoctorAvailable(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);

    /** Check if patient has conflicting appointment */
    boolean hasPatientAppointmentConflict(Long patientId, LocalDate date, LocalTime startTime, LocalTime endTime);
}