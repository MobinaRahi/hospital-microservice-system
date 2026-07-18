package hospital.coreservice.service;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.dto.request.PatientBookingRequest;
import hospital.coreservice.model.enums.AppointmentStatus;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for Appointment management.
 * Handles CRUD, status workflow, scheduling, and availability.
 *
 * @author Mobina
 */
public interface AppointmentService {

    AppointmentResponseDto createAppointment(AppointmentCreateDto createDto);

    AppointmentResponseDto updateAppointment(Long id, AppointmentUpdateDto updateDto);

    void cancelAppointment(Long id, String reason, Long canceledBy);

    void checkInAppointment(Long id);

    void completeAppointment(Long id);

    AppointmentResponseDto getAppointmentById(Long id);

    List<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId);

    List<AppointmentResponseDto> getAppointmentsByDoctorId(Long doctorId);

    List<AppointmentResponseDto> getAppointmentsByDepartmentId(Long departmentId);

    List<AppointmentResponseDto> getAppointmentsByStatus(AppointmentStatus status);

    List<AppointmentResponseDto> getAppointmentsByDate(LocalDate date);

    List<AppointmentResponseDto> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);

    List<AppointmentResponseDto> getTodayAppointments();

    List<AppointmentResponseDto> getThisWeekAppointments();

    List<AppointmentResponseDto> getThisMonthAppointments();

    List<AppointmentResponseDto> getUpcomingAppointments(Long patientId);

    List<AppointmentResponseDto> getPastAppointments(Long patientId);

    List<AppointmentResponseDto> getAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status);

    Long countAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status);

    List<AppointmentResponseDto> getAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status);

    Long countAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status);

    List<AppointmentResponseDto> getAppointmentsByCreatedAtAfter(LocalDateTime createdAt);

    List<AppointmentResponseDto> getAppointmentsByCreatedAtBefore(LocalDateTime createdAt);

    List<AppointmentResponseDto> getAppointmentsByCanceledAtAfter(LocalDateTime canceledAt);

    List<AppointmentResponseDto> getAppointmentsByCanceledAtBefore(LocalDateTime canceledAt);

    List<AppointmentResponseDto> getAppointmentsByCreatedBy(Long createdBy);

    List<AppointmentResponseDto> getAppointmentsByCanceledBy(Long canceledBy);

    List<AppointmentResponseDto> getNoShowAppointments();

    Long countTotalAppointments();

    // ===== تغییر اصلی: LocalDate ورودی، List<LocalTime> خروجی =====
    List<hospital.coreservice.dto.appointment.TimeSlotResponseDto> getAvailableSlots(Long doctorId, LocalDate date);
    // ==============================================================

    AppointmentResponseDto rescheduleAppointment(Long id, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime);

    boolean isDoctorAvailable(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);

    boolean hasPatientAppointmentConflict(Long patientId, LocalDate date, LocalTime startTime, LocalTime endTime);

    AppointmentResponseDto bookAppointmentByPatient(PatientBookingRequest request, Authentication authentication);

    public AppointmentResponseDto getNextAppointment(Long patientId);

    List<AppointmentResponseDto> findAllWithDetails();
}
