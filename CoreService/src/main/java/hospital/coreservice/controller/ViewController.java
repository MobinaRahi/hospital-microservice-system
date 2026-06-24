package hospital.coreservice.controller;

import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.model.Patient;
import hospital.coreservice.security.model.SecurityUser;
import hospital.coreservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * View Controller for serving Thymeleaf HTML pages.
 * Pages are now prepared to receive model data from service layer while keeping
 * safe fallbacks, so UI can be previewed even before database/API setup is complete.
 *
 * @author Mobina
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final AppointmentService appointmentsService;
    private final NurseService nurseService;
    private final RoomService roomService;
    private final ShiftService shiftService;

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
        model.addAttribute("totalAppointmentCount", appointmentsService.countTotalAppointments());
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
        return "dash";
    }

    // ========== Doctor Pages ==========

    @GetMapping("/doctors")
    public String doctors(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String gender,
            Model model) {
        model.addAttribute("doctors", safe(doctorService::getAllDoctors, List.of()));
        model.addAttribute("activeDoctorCount", safe(doctorService::countActiveDoctors, 0L));
        model.addAttribute("inactiveDoctorCount", safe(doctorService::countInactiveDoctors, 0L));
        return "doctors";
    }

    @GetMapping("/doctors/{id}")
    public String doctorProfile(@PathVariable Long id, Model model) {
        model.addAttribute("doctorId", id);
        model.addAttribute("doctor", safe(() -> doctorService.getDoctorById(id), null));
        model.addAttribute("availableSlots", safe(() -> appointmentService.getAvailableSlots(id, LocalDate.now()), List.of()));
        return "doctor-profile";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model) {
        addDoctorPanelModel(model, 1L);
        return "doctor-dashboard";
    }

    @GetMapping("/doctor/appointments")
    public String doctorAppointments(Model model) {
        model.addAttribute("appointments", safe(() -> appointmentService.getAppointmentsByDoctorId(1L), List.of()));
        return "doctor-appointments";
    }

    @GetMapping("/doctor/patients")
    public String doctorPatients(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        return "doctor-patients";
    }

    @GetMapping("/doctor/schedule")
    public String doctorSchedule(Model model) {
        model.addAttribute("doctorId", 1L);
        return "doctor-schedule";
    }

    @GetMapping("/doctor/admitted")
    public String doctorAdmitted(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        return "doctor-admitted";
    }

    @GetMapping("/doctor/records")
    public String doctorRecords(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        return "doctor-records";
    }

    @GetMapping("/doctor/prescriptions")
    public String doctorPrescriptions(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        return "doctor-prescriptions";
    }

    @GetMapping("/doctor/profile")
    public String doctorProfilePage(Model model) {
        model.addAttribute("doctor", safe(() -> doctorService.getDoctorById(1L), null));
        return "doctor-my-profile";
    }

    @GetMapping("/doctor/change-password")
    public String doctorChangePassword(Model model) {
        return "doctor-change-password";
    }

    // ========== Department Pages ==========

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", safe(departmentService::getAllDepartments, List.of()));
        model.addAttribute("activeDepartmentCount", safe(departmentService::countActiveDepartments, 0L));
        return "departments";
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
        model.addAttribute("appointments", safe(appointmentService::getTodayAppointments, List.of()));
        model.addAttribute("appointmentCount", safe(appointmentService::countTotalAppointments, 0L));
        return "appointments";
    }

    // ========== Patient Pages ==========

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", safe(patientService::getAllPatients, List.of()));
        model.addAttribute("activePatientCount", safe(patientService::countActivePatients, 0L));
        return "patients";
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
        return "rooms";
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
        model.addAttribute("todayAppointments", safe(appointmentService::getTodayAppointments, List.of()));
    }

    private void addDoctorPanelModel(Model model, Long doctorId) {
        model.addAttribute("doctor", safe(() -> doctorService.getDoctorById(doctorId), null));
        model.addAttribute("appointments", safe(() -> appointmentService.getAppointmentsByDoctorId(doctorId), List.of()));
        model.addAttribute("availableSlots", safe(() -> appointmentService.getAvailableSlots(doctorId, LocalDate.now()), List.of()));
    }

    private <T> T safe(Callable<T> callable, T fallback) {
        try {
            T value = callable.call();
            return value != null ? value : fallback;
        } catch (Exception ignored) {
            return fallback;
        }
    }
}