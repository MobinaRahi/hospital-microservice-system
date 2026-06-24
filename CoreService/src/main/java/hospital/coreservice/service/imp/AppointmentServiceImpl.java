package hospital.coreservice.service.imp;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.dto.appointment.TimeSlotResponseDto;
import hospital.coreservice.dto.request.PatientBookingRequest;
import hospital.coreservice.exception.appointment.*;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotAvailableException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.exception.patient.PatientAppointmentConflictException;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.mapper.AppointmentMapper;
import hospital.coreservice.model.*;
import hospital.coreservice.model.enums.*;
import hospital.coreservice.repository.*;
import hospital.coreservice.security.model.SecurityUser;
import hospital.coreservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentCreateDto createDto) {
        log.info("Creating new appointment for patient: {} with doctor: {} on {}",
                createDto.getPatientId(), createDto.getDoctorId(), createDto.getAppointmentDate());

        Patient patient = patientRepository.findById(createDto.getPatientId())
                .orElseThrow(() -> PatientNotFoundException.byId(createDto.getPatientId()));

        Doctor doctor = doctorRepository.findById(createDto.getDoctorId())
                .orElseThrow(() -> DoctorNotFoundException.byId(createDto.getDoctorId()));

        Department department = null;
        if (createDto.getDepartmentId() != null) {
            department = departmentRepository.findById(createDto.getDepartmentId())
                    .orElseThrow(() -> DepartmentNotFoundException.byId(createDto.getDepartmentId()));
        }

        // ===== جلوگیری از دابل‌بوکینگ: همین چک قبل از ثبت ضروریه =====
        if (!isDoctorAvailable(createDto.getDoctorId(), createDto.getAppointmentDate(),
                createDto.getStartTime(), createDto.getEndTime())) {
            throw new DoctorNotAvailableException(createDto.getDoctorId(), createDto.getAppointmentDate(), createDto.getStartTime());
        }

        if (hasPatientAppointmentConflict(createDto.getPatientId(), createDto.getAppointmentDate(),
                createDto.getStartTime(), createDto.getEndTime())) {
            throw new PatientAppointmentConflictException(createDto.getPatientId(), createDto.getAppointmentDate(), createDto.getStartTime());
        }

        Appointment appointment = appointmentMapper.toEntity(createDto);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        try {
            Appointment saved = appointmentRepository.save(appointment);
            return appointmentMapper.toResponseDto(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DoctorNotAvailableException(createDto.getDoctorId(), createDto.getAppointmentDate(), createDto.getStartTime());
        }
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

    @Override
    public List<AppointmentResponseDto> getThisWeekAppointments() {
        LocalDate today = LocalDate.now();
        int daysUntilFriday = (5 - today.getDayOfWeek().getValue() + 7) % 7;
        LocalDate endOfWeek = today.plusDays(daysUntilFriday == 0 ? 7 : daysUntilFriday);

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
    public List<AppointmentResponseDto> getAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status) {
        return appointmentRepository.findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status) {
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
    public Long countAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status) {
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

    // ========== Available Slots (اصلاح‌شده) ==========

    @Override
    public List<TimeSlotResponseDto> getAvailableSlots(Long doctorId, LocalDate date) {
        DayOfWeek myDayOfWeek = convertToMyDayOfWeek(date.getDayOfWeek());

        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, myDayOfWeek);

        if (scheduleOpt.isEmpty()) {
            log.warn("Doctor {} has no schedule on {}", doctorId, myDayOfWeek);
            return Collections.emptyList();
        }

        DoctorSchedule schedule = scheduleOpt.get();

        // اگه slotDuration خالی بود، به‌جای کرش، ۳۰ دقیقه پیش‌فرض می‌گیریم
        int slotDuration = schedule.getSlotDuration() != null ? schedule.getSlotDuration() : 30;

        LocalDateTime current = schedule.getStartTime();
        LocalDateTime end = schedule.getEndTime();
        if (current == null || end == null) {
            log.warn("Doctor {} schedule on {} has no start/end time", doctorId, myDayOfWeek);
            return Collections.emptyList();
        }

        // نوبت‌های رزروشده برای این تاریخ؛ لغوشده‌ها دیگه جایی رو اشغال نمی‌کنن
        List<Appointment> booked = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        List<LocalTime> bookedTimes = booked.stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .map(Appointment::getStartTime)   // Appointment.startTime از نوع LocalTime هست
                .toList();

        List<TimeSlotResponseDto> slots = new ArrayList<>();

        while (current.isBefore(end)) {
            LocalTime slotStart = current.toLocalTime();
            LocalDateTime nextDt = current.plusMinutes(slotDuration);
            LocalTime slotEnd = nextDt.toLocalTime();

            if (!bookedTimes.contains(slotStart)) {
                slots.add(new TimeSlotResponseDto(slotStart, slotEnd));
            }
            current = nextDt;
        }

        return slots;
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

    // ========== Reschedule ==========

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

    // ========== Book Appointment by Patient (Guest & Logged-in) ==========

    @Override
    @Transactional
    public AppointmentResponseDto bookAppointmentByPatient(PatientBookingRequest request, Authentication authentication) {

        Long patientId;

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            Patient patient = patientRepository.findByUserId(securityUser.getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found for user: " + securityUser.getUsername()));
            patientId = patient.getId();
        } else {

            Optional<Patient> existingPatient = patientRepository.findByNationalId(request.getNationalId());
            if (existingPatient.isPresent()) {
                patientId = existingPatient.get().getId();
            } else {
                User user = new User();
                user.setUsername(request.getNationalId());
                user.setPasswordHash(passwordEncoder.encode("tempPassword" + System.currentTimeMillis()));
                String[] nameParts = request.getFullName().trim().split("\\s+", 2);
                user.setFirstName(nameParts[0]);
                user.setLastName(nameParts.length > 1 ? nameParts[1] : "");
                user.setEmail(request.getNationalId() + "@temp.hospital.com");
                user.setPhoneNumber(request.getPhone());
                user.setNationalId(request.getNationalId());
                user.setEnabled(true);
                user.setAccountNonLocked(true);
                user.setBirthDate(LocalDate.now().minusYears(30));

                Role patientRole = roleRepository.findByName(RoleName.PATIENT)
                        .orElseThrow(() -> new RuntimeException("Role PATIENT not found"));
                user.setRoles(Set.of(patientRole));
                user = userRepository.save(user);

                Patient patient = new Patient();
                patient.setUser(user);
                patient.setFirstName(user.getFirstName());
                patient.setLastName(user.getLastName());
                patient.setPhoneNumber(request.getPhone());
                patient.setNationalId(request.getNationalId());
                patient.setStatus(PatientStatus.ACTIVE);
                patient.setGender(Gender.UNKNOWN);
                patient.setBloodType(BloodType.UNKNOWN);
                patient.setAddress("ثبت نشده");
                patient.setBirthDate(LocalDate.now().minusYears(30));

                patient = patientRepository.save(patient);
                patientId = patient.getId();
            }
        }

        // ===== ۳. ساخت Appointment =====
        AppointmentCreateDto createDto = new AppointmentCreateDto();
        createDto.setPatientId(patientId);
        createDto.setDoctorId(request.getDoctorId());
        createDto.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        createDto.setStartTime(LocalTime.parse(request.getStartTime()));
        createDto.setEndTime(LocalTime.parse(request.getEndTime()));
        createDto.setType(AppointmentType.valueOf(request.getType()));
        createDto.setReason(request.getReason());
        createDto.setNotes(request.getNotes());

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getDepartment() == null) {
            throw new RuntimeException("Doctor does not have a department assigned");
        }
        createDto.setDepartmentId(doctor.getDepartment().getId());

        return createAppointment(createDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingByPatientId(
                patientId, LocalDate.now(), AppointmentStatus.CANCELLED);
        return appointments.stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getPastAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findPastByPatientId(
                patientId, LocalDate.now(), AppointmentStatus.CANCELLED);
        return appointments.stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getNextAppointment(Long patientId) {
        List<Appointment> results = appointmentRepository.findFirstUpcomingByPatientId(
                patientId, LocalDate.now(), AppointmentStatus.CANCELLED
        );
        if (results.isEmpty()) return null;
        return appointmentMapper.toResponseDto(results.get(0));
    }
}
