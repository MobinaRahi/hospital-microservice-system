package hospital.coreservice.service.imp;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.exception.appointment.*;
import hospital.coreservice.exception.doctor.DoctorNotAvailableException;
import hospital.coreservice.exception.doctor_schedule.DoctorScheduleNotFoundException;
import hospital.coreservice.exception.patient.PatientAppointmentConflictException;
import hospital.coreservice.mapper.AppointmentMapper;
import hospital.coreservice.model.Appointment;
import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.repository.AppointmentRepository;
import hospital.coreservice.repository.DoctorScheduleRepository;
import hospital.coreservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AppointmentService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorScheduleRepository doctorScheduleRepository;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentCreateDto createDto) {
        // Check doctor availability
        if (!isDoctorAvailable(createDto.getDoctorId(), createDto.getAppointmentDate(),
                createDto.getStartTime(), createDto.getEndTime())) {
            throw new DoctorNotAvailableException(createDto.getDoctorId(),
                    createDto.getAppointmentDate(), createDto.getStartTime());
        }

        // Check patient conflict
        if (hasPatientAppointmentConflict(createDto.getPatientId(), createDto.getAppointmentDate(),
                createDto.getStartTime(), createDto.getEndTime())) {
            throw new PatientAppointmentConflictException(createDto.getPatientId(),
                    createDto.getAppointmentDate(), createDto.getStartTime());
        }

        Appointment appointment = appointmentMapper.toEntity(createDto);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto updateAppointment(Long id, AppointmentUpdateDto updateDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));

        appointmentMapper.updateEntity(appointment, updateDto);
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id, String reason, Long canceledBy) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new InvalidCancelStateException(id, appointment.getStatus());
        }

        appointmentRepository.cancelAppointment(id, reason, canceledBy);
    }

    @Override
    @Transactional
    public void checkInAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new InvalidCheckInStateException(id, appointment.getStatus());
        }

        appointmentRepository.updateStatus(id, AppointmentStatus.CHECK_IN);
    }

    @Override
    @Transactional
    public void completeAppointment(Long id) {
        log.info("Completing appointment with id: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));

        if (appointment.getStatus() != AppointmentStatus.CHECK_IN) {
            throw new InvalidCompleteStateException(id, appointment.getStatus());
        }

        appointmentRepository.updateStatus(id, AppointmentStatus.COMPLETED);
    }
    // ========== Basic Retrieval ==========

    @Override
    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));
        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDepartmentId(Long departmentId) {
        return appointmentRepository.findByDepartmentId(departmentId)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Date Based Retrieval ==========

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointmentRepository.findByAppointmentDateBetween(startDate, endDate)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getTodayAppointments() {
        return appointmentRepository.findByAppointmentDate(LocalDate.now())
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /// Ask
    @Override
    public List<AppointmentResponseDto> getThisWeekAppointments() {
        LocalDate today = LocalDate.now();
        int daysUntilFriday = 5 - today.getDayOfWeek().getValue();
        LocalDate endOfWeek = today.plusDays(daysUntilFriday);

        return appointmentRepository.findByAppointmentDateBetween(today, endOfWeek)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getThisMonthAppointments() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return appointmentRepository.findByAppointmentDateBetween(startOfMonth, endOfMonth)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Patient Specific ==========

    @Override
    public List<AppointmentResponseDto> getUpcomingAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdAndAppointmentDateGreaterThanEqual(patientId, LocalDate.now())
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getPastAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdAndAppointmentDateLessThanEqual(patientId, LocalDate.now())
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status) {
        return appointmentRepository.findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countAppointmentsByPatient(Long patientId, AppointmentStatus status) {
        return appointmentRepository.countByPatientIdAndStatus(patientId, status);
    }

    // ========== Doctor Specific ==========

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countAppointmentsByDoctor(Long doctorId, AppointmentStatus status) {
        return appointmentRepository.countByDoctorIdAndStatus(doctorId, status);
    }

    // ========== Audit / User Tracking ==========

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCreatedAtAfter(LocalDateTime createdAt) {
        return appointmentRepository.findByCreatedAtAfter(createdAt)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCreatedAtBefore(LocalDateTime createdAt) {
        return appointmentRepository.findByCreatedAtBefore(createdAt)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCanceledAtAfter(LocalDateTime canceledAt) {
        return appointmentRepository.findByCanceledAtAfter(canceledAt)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCanceledAtBefore(LocalDateTime canceledAt) {
        return appointmentRepository.findByCanceledAtBefore(canceledAt)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCreatedBy(Long createdBy) {
        return appointmentRepository.findByCreatedBy(createdBy)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByCanceledBy(Long canceledBy) {
        return appointmentRepository.findByCanceledBy(canceledBy)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Special Cases ==========

    @Override
    public List<AppointmentResponseDto> getNoShowAppointments() {
        return appointmentRepository.findByStatus(AppointmentStatus.NO_SHOW)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Statistics ==========

    @Override
    public Long countTotalAppointments() {
        return appointmentRepository.count();
    }

    // ========== Features ==========

    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        DayOfWeek myDayOfWeek = convertToMyDayOfWeek(date.getDayOfWeek());
        DoctorSchedule schedule = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, myDayOfWeek)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byDoctorIdAndDayOfWeek(doctorId, myDayOfWeek));

        List<Appointment> booked = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();
        while (current.isBefore(schedule.getEndTime())) {
            slots.add(current);
            current = current.plusMinutes(schedule.getSlotDuration());
        }

        List<LocalTime> available = new ArrayList<>(slots);
        for (Appointment app : booked) {
            available.remove(app.getStartTime());
        }
        return available;
    }

    public DayOfWeek convertToMyDayOfWeek(java.time.DayOfWeek javaDay) {
        return switch (javaDay) {
            case SATURDAY -> DayOfWeek.SATURDAY;
            case SUNDAY -> DayOfWeek.SUNDAY;
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
        };
    }

    @Override
    @Transactional
    public AppointmentResponseDto rescheduleAppointment(Long id, LocalDate newDate,
                                                        LocalTime newStartTime, LocalTime newEndTime) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> AppointmentNotFoundException.byId(id));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new InvalidAppointmentStateException(id, appointment.getStatus());
        }

        Long doctorId = appointment.getDoctor().getId();
        Long patientId = appointment.getPatient().getId();

        if (!isDoctorAvailable(doctorId, newDate, newStartTime, newEndTime)) {
            throw new DoctorNotAvailableException(doctorId, newDate, newStartTime);
        }

        if (hasPatientAppointmentConflict(patientId, newDate, newStartTime, newEndTime)) {
            throw new PatientAppointmentConflictException(patientId, newDate, newStartTime);
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDto(saved);
    }

    // ========== Validation ==========

    @Override
    public boolean isDoctorAvailable(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        DayOfWeek myDayOfWeek = convertToMyDayOfWeek(date.getDayOfWeek());

        boolean hasSchedule = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, myDayOfWeek)
                .isPresent();

        if (!hasSchedule) return false;

        return appointmentRepository
                .findByDoctorIdAndAppointmentDate(doctorId, date)
                .stream()
                .noneMatch(a -> a.getStartTime().equals(startTime));
    }

    @Override
    public boolean hasPatientAppointmentConflict(Long patientId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return appointmentRepository
                .findByPatientIdAndAppointmentDate(patientId, date)
                .stream()
                .anyMatch(a -> a.getStartTime().equals(startTime));
    }
}

