package hospital.coreservice.controller;

import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.mapper.AppointmentMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import hospital.coreservice.repository.AppointmentRepository;
import hospital.coreservice.security.model.SecurityUser;
import hospital.coreservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * View Controller for serving Thymeleaf HTML pages.
 * Pages are now prepared to receive model data from service layer while keeping
 * safe fallbacks, so UI can be previewed even before database/API setup is complete.
 *
 * @author Mobina
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ViewController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final NurseService nurseService;
    private final RoomService roomService;
    private final ShiftService shiftService;
    private final DoctorScheduleService doctorScheduleService;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;

    // ========== Public Pages ==========

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        addPublicStats(model);
        // اضافه کردن لیست پزشکان و بخش‌ها برای نمایش در صفحه اصلی و فرم جستجو
        // پزشکان فعال
        model.addAttribute("doctors", doctorService.getActiveDoctors());

        // بخش‌ها
        model.addAttribute("departments", departmentService.getActiveDepartments());

        // آمار
        model.addAttribute("activeDoctorCount", doctorService.countActiveDoctors());
        model.addAttribute("activePatientCount", patientService.countActivePatients());
        model.addAttribute("activeDepartmentCount", departmentService.countActiveDepartments());
        model.addAttribute("totalAppointmentCount", appointmentService.countTotalAppointments());
        return "index";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) Long patientId,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        // اگه از مرحله‌ی ساخت یوزر مهمان (مثلا موقع گرفتن نوبت) به اینجا اومده باشه،
        // patientId رو نگه می‌داریم تا بتونیم به صفحه‌ی تکمیل ثبت‌نام پاس بدیمش
        if (patientId != null) {
            model.addAttribute("patientId", patientId);
        }
        return "login";
    }

    @GetMapping("/patient/complete-profile")
    public String completeProfile(
            @RequestParam(required = false) Long patientId,
            Authentication authentication,
            Model model) {

        Object patient = null;

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            patient = patientService.getPatientByUserId(securityUser.getId())
                    .orElseThrow(() -> PatientNotFoundException.byUserId(securityUser.getId()));
        }
        else if (patientId != null) {
            patient = patientService.getPatientById(patientId);
        }
        else {
            return "redirect:/";
        }

        model.addAttribute("patient", patient);
        return "patient-profile-complete";
    }
    // ========== Dashboard ==========

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        addDashboardModel(model);
        return "dashboard";
    }

    // ========== Doctor Pages ==========

    @GetMapping("/doctors")
    public String doctors(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String gender,
            Model model) {

        List<DoctorResponseDto> allDocs = safe(doctorService::getAllDoctors, List.of());
        long activeCount = safe(doctorService::countActiveDoctors, 0L);
        long inactiveCount = safe(doctorService::countInactiveDoctors, 0L);

        // Calculate dynamic stats
        model.addAttribute("doctors", allDocs);
        model.addAttribute("totalDoctorCount", allDocs.size());
        model.addAttribute("activeDoctorCount", activeCount);
        model.addAttribute("inactiveDoctorCount", inactiveCount);
        model.addAttribute("todayAppointmentsCount", safe(appointmentService::countTotalAppointments, 0L));
        model.addAttribute("specialityLabels", specialityLabels());
        model.addAttribute("subSpecialityLabels", subSpecialityLabels());

        return "doctors";
    }

    @GetMapping("/doctors/add")
    public String addDoctor(Model model) {
        model.addAttribute("departments", safe(departmentService::getAllDepartments, List.of()));
        return "add-doctor";
    }

    @GetMapping("/doctors/{id}")
    public String doctorProfile(@PathVariable Long id, Model model) {
        model.addAttribute("doctorId", id);
        model.addAttribute("doctor", safe(() -> doctorService.getDoctorById(id), null));
        model.addAttribute("availableSlots", safe(() -> appointmentService.getAvailableSlots(id, LocalDate.now()), List.of()));
        model.addAttribute("specialityLabels", specialityLabels());
        model.addAttribute("subSpecialityLabels", subSpecialityLabels());
        return "doctor-profile";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            Authentication authentication,
            Model model) {

        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        DoctorPanelData data = buildDoctorPanelData(doctor, selectedDate);

        List<AppointmentResponseDto> filteredTodayAppointments = data.todayAppointments().stream()
                .filter(appointment -> status == null || appointment.getStatus() == status)
                .filter(appointment -> matchesAppointment(appointment, q))
                .collect(Collectors.toList());

        addDoctorCommonModel(model, doctor, data, selectedDate, q);
        model.addAttribute("status", status);
        model.addAttribute("todayAppointments", filteredTodayAppointments);
        model.addAttribute("upcomingAppointments", data.upcomingAppointments().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("doctorPatients", data.patients().stream().limit(6).collect(Collectors.toList()));
        model.addAttribute("admittedPatients", data.admittedPatients().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("schedules", data.schedules());
        model.addAttribute("availableSlots", data.availableSlots());
        return "doctor-dashboard";
    }

    @GetMapping("/doctor/appointments")
    public String doctorAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) AppointmentType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication,
            Model model) {

        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());

        List<AppointmentResponseDto> appointments = data.appointments().stream()
                .filter(appointment -> matchesAppointment(appointment, q))
                .filter(appointment -> status == null || appointment.getStatus() == status)
                .filter(appointment -> type == null || appointment.getType() == type)
                .filter(appointment -> from == null || (appointment.getAppointmentDate() != null && !appointment.getAppointmentDate().isBefore(from)))
                .filter(appointment -> to == null || (appointment.getAppointmentDate() != null && !appointment.getAppointmentDate().isAfter(to)))
                .sorted(Comparator
                        .comparing(AppointmentResponseDto::getAppointmentDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()
                        .thenComparing(AppointmentResponseDto::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        addDoctorCommonModel(model, doctor, data, LocalDate.now(), q);
        model.addAttribute("appointments", appointments);
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("appointmentCount", appointments.size());
        model.addAttribute("scheduledCount", countByStatus(appointments, AppointmentStatus.SCHEDULED));
        model.addAttribute("checkInCount", countByStatus(appointments, AppointmentStatus.CHECK_IN));
        model.addAttribute("completedCount", countByStatus(appointments, AppointmentStatus.COMPLETED));
        model.addAttribute("cancelledCount", countByStatus(appointments, AppointmentStatus.CANCELLED));
        return "doctor-appointments";
    }

    @GetMapping("/doctor/patients")
    public String doctorPatients(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) PatientStatus status,
            @RequestParam(required = false) Gender gender,
            Authentication authentication,
            Model model) {

        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());

        List<PatientResponseDto> patients = data.patients().stream()
                .filter(patient -> matchesPatient(patient, q))
                .filter(patient -> status == null || patient.getStatus() == status)
                .filter(patient -> gender == null || patient.getGender() == gender)
                .collect(Collectors.toList());

        addDoctorCommonModel(model, doctor, data, LocalDate.now(), q);
        model.addAttribute("patients", patients);
        model.addAttribute("status", status);
        model.addAttribute("gender", gender);
        model.addAttribute("lastAppointments", lastAppointmentByPatient(data.appointments()));
        model.addAttribute("patientCount", patients.size());
        model.addAttribute("activePatientCount", patients.stream().filter(p -> p.getStatus() == PatientStatus.ACTIVE).count());
        model.addAttribute("admittedPatientCount", patients.stream().filter(p -> p.getCurrentRoom() != null).count());
        return "doctor-patients";
    }

    @GetMapping("/doctor/schedule")
    public String doctorSchedule(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication,
            Model model) {

        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        DoctorPanelData data = buildDoctorPanelData(doctor, selectedDate);

        addDoctorCommonModel(model, doctor, data, selectedDate, null);
        model.addAttribute("schedules", data.schedules());
        model.addAttribute("availableSlots", data.availableSlots());
        model.addAttribute("selectedDateAppointments", data.appointments().stream()
                .filter(appointment -> selectedDate.equals(appointment.getAppointmentDate()))
                .sorted(Comparator.comparing(AppointmentResponseDto::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList()));
        return "doctor-schedule";
    }

    @GetMapping("/doctor/admitted")
    public String doctorAdmitted(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String q,
            Authentication authentication,
            Model model) {
        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());
        addDoctorCommonModel(model, doctor, data, LocalDate.now(), q);
        model.addAttribute("patients", data.admittedPatients().stream()
                .filter(patient -> matchesPatient(patient, q))
                .collect(Collectors.toList()));
        return "doctor-admitted";
    }

    @GetMapping("/doctor/records")
    public String doctorRecords(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String q,
            Authentication authentication,
            Model model) {
        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());
        addDoctorCommonModel(model, doctor, data, LocalDate.now(), q);
        model.addAttribute("patients", data.patients().stream()
                .filter(patient -> matchesPatient(patient, q))
                .collect(Collectors.toList()));
        return "doctor-records";
    }

    @GetMapping("/doctor/prescriptions")
    public String doctorPrescriptions(
            @RequestParam(required = false) Long doctorId,
            Authentication authentication,
            Model model) {
        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());
        addDoctorCommonModel(model, doctor, data, LocalDate.now(), null);
        model.addAttribute("patients", data.patients());
        return "doctor-prescriptions";
    }

    @GetMapping("/doctor/profile")
    public String doctorProfilePage(
            @RequestParam(required = false) Long doctorId,
            Authentication authentication,
            Model model) {
        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        DoctorPanelData data = buildDoctorPanelData(doctor, LocalDate.now());
        addDoctorCommonModel(model, doctor, data, LocalDate.now(), null);
        return "doctor-my-profile";
    }

    @GetMapping("/doctor/change-password")
    public String doctorChangePassword(
            @RequestParam(required = false) Long doctorId,
            Authentication authentication,
            Model model) {
        DoctorResponseDto doctor = resolveDoctor(authentication, doctorId);
        model.addAttribute("doctor", doctor);
        return "doctor-change-password";
    }

    // ========== Department Pages ==========

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", safe(departmentService::getAllDepartments, List.of()));
        model.addAttribute("activeDepartmentCount", safe(departmentService::countActiveDepartments, 0L));
        model.addAttribute("activeDoctorCount", safe(doctorService::countActiveDoctors, 0L));
        return "departments";
    }

    @GetMapping("/departments/add")
    public String addDepartment(Model model) {
        return "add-department";
    }

    // ========== Appointment Pages ==========

    @GetMapping("/appointments/book")
    public String bookAppointment(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            Model model) {
        if (doctorId != null) model.addAttribute("preselectedDoctorId", doctorId);
        if (departmentId != null) model.addAttribute("preselectedDepartmentId", departmentId);
        model.addAttribute("doctors", safe(doctorService::getActiveDoctors, List.of()));
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        model.addAttribute("departments", safe(departmentService::getActiveDepartments, List.of()));
        return "book-appointment";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        List<hospital.coreservice.dto.appointment.AppointmentResponseDto> allAppointments = safe(() -> {
            return appointmentRepository.findAllWithDetails()
                    .stream()
                    .map(appointmentMapper::toResponseDto)
                    .collect(java.util.stream.Collectors.toList());
        }, List.of());

        long scheduledCount = allAppointments.stream().filter(a -> a.getStatus().name().equals("SCHEDULED")).count();
        long completedCount = allAppointments.stream().filter(a -> a.getStatus().name().equals("COMPLETED")).count();
        long checkedInCount = allAppointments.stream().filter(a -> a.getStatus().name().equals("CHECK_IN")).count();
        long cancelledCount = allAppointments.stream().filter(a -> a.getStatus().name().equals("CANCELLED")).count();

        model.addAttribute("appointments", allAppointments);
        model.addAttribute("appointmentCount", allAppointments.size());
        model.addAttribute("scheduledCount", scheduledCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("checkedInCount", checkedInCount);
        model.addAttribute("cancelledCount", cancelledCount);
        return "appointments";
    }

    // ========== Patient Pages ==========

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        model.addAttribute("activePatientCount", safe(patientService::countActivePatients, 0L));
        return "patients";
    }

    @GetMapping("/patients/add")
    public String addPatient(Model model) {
        return "add-patient";
    }

    @GetMapping("/patient_dashboard")
    public String patientDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            Patient patient = patientService.getPatientByUserId(securityUser.getId())
                    .orElseThrow(() -> PatientNotFoundException.byUserId(securityUser.getId()));

            // ID رو از entity بگیر، بقیه رو از DTO
            PatientResponseDto patientDto = patientService.getPatientById(patient.getId());

            model.addAttribute("patient", patientDto);
            model.addAttribute("upcomingAppointments",
                    appointmentService.getUpcomingAppointments(patient.getId()));
            model.addAttribute("pastAppointments",
                    appointmentService.getPastAppointments(patient.getId()));
            model.addAttribute("nextAppointment",
                    appointmentService.getNextAppointment(patient.getId()));
        } else {
            model.addAttribute("patient", null);
            model.addAttribute("upcomingAppointments", List.of());
            model.addAttribute("pastAppointments", List.of());
            model.addAttribute("nextAppointment", null);
        }
        return "patient-dashboard";
    }
    // ========== Nurse Pages ==========

    @GetMapping("/nurses")
    public String nurses(Model model) {
        model.addAttribute("nurses", safe(nurseService::getAllNurses, List.of()));
        model.addAttribute("activeNurseCount", safe(nurseService::countActiveNurses, 0L));
        return "nurses";
    }

    @GetMapping("/nurses/add")
    public String addNurse(Model model) {
        model.addAttribute("departments", safe(departmentService::getAllDepartments, List.of()));
        return "add-nurse";
    }

    @GetMapping("/nurse/dashboard")
    public String nurseDashboard(Model model) {
        model.addAttribute("nurse", safe(() -> nurseService.getNurseById(1L), null));
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        return "nurse-dashboard";
    }

    // ========== Room Pages ==========

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", safe(roomService::getAllRooms, List.of()));
        model.addAttribute("availableRoomCount", safe(roomService::countAvailableRooms, 0L));
        model.addAttribute("occupiedRoomCount", safe(roomService::countOccupiedRooms, 0L));
        model.addAttribute("departments", safe(departmentService::getActiveDepartments, List.of()));
        return "rooms";
    }

    @GetMapping("/rooms/add")
    public String addRoom(Model model) {
        model.addAttribute("departments", safe(departmentService::getActiveDepartments, List.of()));
        return "add-room";
    }

    // ========== Shift Pages ==========

    @GetMapping("/shifts")
    public String shifts(Model model) {
        model.addAttribute("shifts", safe(shiftService::getAllShifts, List.of()));
        model.addAttribute("activeShiftCount", safe(shiftService::countActiveShifts, 0L));
        return "shifts";
    }

    @GetMapping("/patient/book")
    public String patientBookAppointment(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            Model model) {

        if (doctorId != null) {
            model.addAttribute("preselectedDoctorId", doctorId);
        }
        if (departmentId != null) {
            model.addAttribute("preselectedDepartmentId", departmentId);
        }

        model.addAttribute("departments", departmentService.getActiveDepartments());

        model.addAttribute("doctors", doctorService.getActiveDoctors());

        return "patient-book";
    }

    // ========== Private Helpers ==========

    private void addPublicStats(Model model) {
        model.addAttribute("activeDoctorCount", safe(doctorService::countActiveDoctors, 0L));
        model.addAttribute("activePatientCount", safe(patientService::countActivePatients, 0L));
        model.addAttribute("activeDepartmentCount", safe(departmentService::countActiveDepartments, 0L));
    }

    private void addDashboardModel(Model model) {
        model.addAttribute("activeDoctorCount", safe(doctorService::countActiveDoctors, 0L));
        model.addAttribute("activePatientCount", safe(patientService::countActivePatients, 0L));
        model.addAttribute("availableRoomCount", safe(roomService::countAvailableRooms, 0L));
        model.addAttribute("occupiedRoomCount", safe(roomService::countOccupiedRooms, 0L));
        model.addAttribute("activeNurseCount", safe(nurseService::countActiveNurses, 0L));
        model.addAttribute("appointmentCount", safe(appointmentService::countTotalAppointments, 0L));

        // Use recent appointments regardless of date to show some data in dashboard
        List<AppointmentResponseDto> recentAppointments =
                safe(() -> {
                    return appointmentRepository.findAll(org.springframework.data.domain.PageRequest.of(0, 10,
                                    org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")))
                            .stream()
                            .map(appointmentMapper::toResponseDto)
                            .collect(java.util.stream.Collectors.toList());
                }, List.of());

        model.addAttribute("todayAppointments", recentAppointments);
        model.addAttribute("doctors", safe(doctorService::getActiveDoctors, List.of()));
    }

    private DoctorResponseDto resolveDoctor(Authentication authentication, Long requestedDoctorId) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            DoctorResponseDto authenticatedDoctor = safe(() -> doctorService.getDoctorByUserId(securityUser.getId()), null);
            if (authenticatedDoctor != null) {
                return authenticatedDoctor;
            }
        }

        if (requestedDoctorId != null) {
            DoctorResponseDto requestedDoctor = safe(() -> doctorService.getDoctorById(requestedDoctorId), null);
            if (requestedDoctor != null) {
                return requestedDoctor;
            }
        }

        // فقط برای جلوگیری از خالی ماندن UI در محیط dev/preview؛ در حالت واقعی کاربر پزشک از session خوانده می‌شود.
        return safe(() -> doctorService.getDoctorById(1L), null);
    }

    private DoctorPanelData buildDoctorPanelData(DoctorResponseDto doctor, LocalDate selectedDate) {
        if (doctor == null || doctor.getId() == null) {
            return DoctorPanelData.empty();
        }

        List<AppointmentResponseDto> appointments = doctorAppointmentsWithDetails(doctor.getId());
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        List<AppointmentResponseDto> todayAppointments = appointments.stream()
                .filter(appointment -> selectedDate.equals(appointment.getAppointmentDate()))
                .sorted(Comparator.comparing(AppointmentResponseDto::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        List<AppointmentResponseDto> upcomingAppointments = appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate() != null)
                .filter(appointment -> !appointment.getAppointmentDate().isBefore(today))
                .filter(appointment -> appointment.getStatus() != AppointmentStatus.CANCELLED)
                .sorted(Comparator
                        .comparing(AppointmentResponseDto::getAppointmentDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(AppointmentResponseDto::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        List<PatientResponseDto> patients = uniquePatients(appointments);
        List<PatientResponseDto> admittedPatients = patients.stream()
                .filter(patient -> patient.getCurrentRoom() != null)
                .collect(Collectors.toList());

        List<DoctorScheduleResponseDto> schedules = this.<List<DoctorScheduleResponseDto>>safe(
                        () -> doctorScheduleService.getActiveDoctorSchedulesByDoctorId(doctor.getId()),
                        List.of()
                ).stream()
                .sorted(Comparator.comparing(schedule -> schedule.getDayOfWeek() != null ? schedule.getDayOfWeek().ordinal() : 99))
                .collect(Collectors.toList());

        List<?> availableSlots = safe(() -> appointmentService.getAvailableSlots(doctor.getId(), selectedDate), List.of());

        long weekPatientCount = appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate() != null)
                .filter(appointment -> !appointment.getAppointmentDate().isBefore(today))
                .filter(appointment -> !appointment.getAppointmentDate().isAfter(today.plusDays(7)))
                .map(AppointmentResponseDto::getPatient)
                .filter(Objects::nonNull)
                .map(PatientResponseDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        long monthAppointmentCount = appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate() != null)
                .filter(appointment -> YearMonth.from(appointment.getAppointmentDate()).equals(currentMonth))
                .count();

        return new DoctorPanelData(
                appointments,
                todayAppointments,
                upcomingAppointments,
                patients,
                admittedPatients,
                schedules,
                availableSlots,
                weekPatientCount,
                monthAppointmentCount
        );
    }

    private List<AppointmentResponseDto> doctorAppointmentsWithDetails(Long doctorId) {
        return safe(() -> appointmentRepository.findByDoctorIdWithDetails(doctorId)
                .stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList()), List.of());
    }

    private void addDoctorCommonModel(Model model,
                                      DoctorResponseDto doctor,
                                      DoctorPanelData data,
                                      LocalDate selectedDate,
                                      String q) {
        model.addAttribute("doctor", doctor);
        model.addAttribute("doctorId", doctor != null ? doctor.getId() : null);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("q", q);
        model.addAttribute("appointments", data.appointments());
        model.addAttribute("todayAppointmentCount", data.todayAppointments().size());
        model.addAttribute("upcomingAppointmentCount", data.upcomingAppointments().size());
        model.addAttribute("weekPatientCount", data.weekPatientCount());
        model.addAttribute("monthAppointmentCount", data.monthAppointmentCount());
        model.addAttribute("patientCount", data.patients().size());
        model.addAttribute("admittedPatientCount", data.admittedPatients().size());
        model.addAttribute("availableSlotCount", data.availableSlots().size());
        model.addAttribute("scheduledCount", countByStatus(data.appointments(), AppointmentStatus.SCHEDULED));
        model.addAttribute("checkInCount", countByStatus(data.appointments(), AppointmentStatus.CHECK_IN));
        model.addAttribute("completedCount", countByStatus(data.appointments(), AppointmentStatus.COMPLETED));
        model.addAttribute("cancelledCount", countByStatus(data.appointments(), AppointmentStatus.CANCELLED));
        model.addAttribute("appointmentStatuses", AppointmentStatus.values());
        model.addAttribute("appointmentTypes", AppointmentType.values());
        model.addAttribute("patientStatuses", PatientStatus.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("statusLabels", appointmentStatusLabels());
        model.addAttribute("typeLabels", appointmentTypeLabels());
        model.addAttribute("patientStatusLabels", patientStatusLabels());
        model.addAttribute("genderLabels", genderLabels());
        model.addAttribute("dayLabels", dayLabels());
        model.addAttribute("specialityLabels", specialityLabels());
        model.addAttribute("subSpecialityLabels", subSpecialityLabels());
    }

    private List<PatientResponseDto> uniquePatients(List<AppointmentResponseDto> appointments) {
        return appointments.stream()
                .map(AppointmentResponseDto::getPatient)
                .filter(Objects::nonNull)
                .filter(patient -> patient.getId() != null)
                .collect(Collectors.toMap(
                        PatientResponseDto::getId,
                        Function.identity(),
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    private Map<Long, AppointmentResponseDto> lastAppointmentByPatient(List<AppointmentResponseDto> appointments) {
        return appointments.stream()
                .filter(appointment -> appointment.getPatient() != null && appointment.getPatient().getId() != null)
                .sorted(Comparator
                        .comparing(AppointmentResponseDto::getAppointmentDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()
                        .thenComparing(AppointmentResponseDto::getStartTime))
                .collect(Collectors.toMap(
                        appointment -> appointment.getPatient().getId(),
                        Function.identity(),
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ));
    }

    private long countByStatus(List<AppointmentResponseDto> appointments, AppointmentStatus status) {
        return appointments.stream()
                .filter(appointment -> appointment.getStatus() == status)
                .count();
    }

    private boolean matchesAppointment(AppointmentResponseDto appointment, String keyword) {
        if (isBlank(keyword)) {
            return true;
        }
        PatientResponseDto patient = appointment.getPatient();
        String haystack = String.join(" ",
                stringValue(patient != null ? patient.getFullName() : null),
                stringValue(patient != null ? patient.getFirstName() : null),
                stringValue(patient != null ? patient.getLastName() : null),
                stringValue(patient != null ? patient.getNationalId() : null),
                stringValue(patient != null ? patient.getPhoneNumber() : null),
                stringValue(appointment.getReason()),
                stringValue(appointment.getNotes()),
                stringValue(appointment.getStatus()),
                stringValue(appointment.getType())
        );
        return normalize(haystack).contains(normalize(keyword));
    }

    private boolean matchesPatient(PatientResponseDto patient, String keyword) {
        if (isBlank(keyword)) {
            return true;
        }
        String haystack = String.join(" ",
                stringValue(patient.getFullName()),
                stringValue(patient.getFirstName()),
                stringValue(patient.getLastName()),
                stringValue(patient.getNationalId()),
                stringValue(patient.getPhoneNumber()),
                stringValue(patient.getEmail()),
                stringValue(patient.getUsername()),
                stringValue(patient.getBloodType()),
                stringValue(patient.getAllergies()),
                stringValue(patient.getStatus()),
                stringValue(patient.getGender())
        );
        return normalize(haystack).contains(normalize(keyword));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String stringValue(Object value) {
        return value == null ? "" : value.toString();
    }

    private String normalize(String value) {
        return value == null ? "" : value
                .toLowerCase(Locale.ROOT)
                .replace('ي', 'ی')
                .replace('ك', 'ک')
                .trim();
    }

    private Map<String, String> appointmentStatusLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("SCHEDULED", "رزرو شده");
        labels.put("CHECK_IN", "پذیرش شده");
        labels.put("IN_PROGRESS", "در حال ویزیت");
        labels.put("COMPLETED", "تکمیل شده");
        labels.put("CANCELLED", "لغو شده");
        labels.put("NO_SHOW", "عدم مراجعه");
        return labels;
    }

    private Map<String, String> appointmentTypeLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("IN_PERSON", "حضوری");
        labels.put("VIDEO", "ویدیویی");
        labels.put("PHONE", "تلفنی");
        labels.put("EMERGENCY", "اورژانسی");
        return labels;
    }

    private Map<String, String> patientStatusLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("ACTIVE", "فعال");
        labels.put("ARCHIVED", "آرشیو شده");
        labels.put("DECEASED", "فوت شده");
        labels.put("TRANSFERRED", "منتقل شده");
        labels.put("INACTIVE", "غیرفعال");
        return labels;
    }

    private Map<String, String> genderLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("MAN", "مرد");
        labels.put("FEMALE", "زن");
        labels.put("OTHER", "سایر");
        labels.put("UNKNOWN", "نامشخص");
        return labels;
    }

    private Map<String, String> dayLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("SATURDAY", "شنبه");
        labels.put("SUNDAY", "یکشنبه");
        labels.put("MONDAY", "دوشنبه");
        labels.put("TUESDAY", "سه‌شنبه");
        labels.put("WEDNESDAY", "چهارشنبه");
        labels.put("THURSDAY", "پنجشنبه");
        labels.put("FRIDAY", "جمعه");
        return labels;
    }

    private Map<String, String> specialityLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        for (Speciality speciality : Speciality.values()) {
            labels.put(speciality.name(), switch (speciality) {
                case CARDIOLOGY -> "قلب و عروق";
                case INTERNAL_MEDICINE -> "داخلی";
                case PEDIATRICS -> "اطفال";
                case NEUROLOGY -> "مغز و اعصاب";
                case GASTROENTEROLOGY -> "گوارش";
                case ENDOCRINOLOGY -> "غدد";
                case NEPHROLOGY -> "کلیه";
                case PULMONOLOGY -> "ریه";
                case RHEUMATOLOGY -> "روماتولوژی";
                case INFECTIOUS_DISEASES -> "عفونی";
                case GENERAL_SURGERY -> "جراحی عمومی";
                case CARDIAC_SURGERY -> "جراحی قلب";
                case NEUROSURGERY -> "جراحی مغز و اعصاب";
                case ORTHOPEDICS -> "ارتوپدی";
                case PLASTIC_SURGERY -> "جراحی پلاستیک";
                case RADIOLOGY -> "رادیولوژی";
                case PATHOLOGY -> "پاتولوژی";
                case ANESTHESIOLOGY -> "بیهوشی";
                case EMERGENCY_MEDICINE -> "اورژانس";
                case FAMILY_MEDICINE -> "پزشکی خانواده";
                case PSYCHIATRY -> "روان‌پزشکی";
            });
        }
        return labels;
    }

    private Map<String, String> subSpecialityLabels() {
        Map<String, String> labels = new LinkedHashMap<>();
        for (SubSpeciality subSpeciality : SubSpeciality.values()) {
            labels.put(subSpeciality.name(), switch (subSpeciality) {
                case INTERVENTIONAL_CARDIOLOGY -> "قلب مداخله‌ای";
                case ECHOCARDIOGRAPHY -> "اکوکاردیوگرافی";
                case ELECTROPHYSIOLOGY -> "الکتروفیزیولوژی";
                case GASTROENTEROLOGY -> "گوارش";
                case HEPATOLOGY -> "کبد";
                case NEPHROLOGY -> "نفرولوژی";
                case STROKE -> "سکته مغزی";
                case EPILEPSY -> "صرع";
                case MOVEMENT_DISORDERS -> "اختلالات حرکتی";
                case NONE -> "بدون فوق‌تخصص";
            });
        }
        return labels;
    }

    private record DoctorPanelData(
            List<AppointmentResponseDto> appointments,
            List<AppointmentResponseDto> todayAppointments,
            List<AppointmentResponseDto> upcomingAppointments,
            List<PatientResponseDto> patients,
            List<PatientResponseDto> admittedPatients,
            List<DoctorScheduleResponseDto> schedules,
            List<?> availableSlots,
            long weekPatientCount,
            long monthAppointmentCount
    ) {
        static DoctorPanelData empty() {
            return new DoctorPanelData(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), 0L, 0L);
        }
    }

    private <T> T safe(Callable<T> callable, T fallback) {
        try {
            T value = callable.call();
            return value != null ? value : fallback;
        } catch (Exception e) {
            log.error("safe() fallback triggered due to exception", e);
            return fallback;
        }
    }
}