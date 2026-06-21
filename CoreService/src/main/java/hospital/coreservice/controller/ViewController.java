package hospital.coreservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * View Controller for serving Thymeleaf HTML pages.
 * All REST API endpoints are handled by their respective *Api controllers.
 *
 * @author Mobina
 */
@Controller
public class ViewController {

    // ========== Public Pages ==========

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    // ========== Dashboard ==========

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    // ========== Doctor Pages ==========

    @GetMapping("/doctors")
    public String doctors(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String gender,
            Model model) {
        return "doctors";
    }

    @GetMapping("/doctors/{id}")
    public String doctorProfile(@PathVariable Long id, Model model) {
        model.addAttribute("doctorId", id);
        return "doctor-profile";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model) {
        return "doctor-dashboard";
    }

    @GetMapping("/doctor/appointments")
    public String doctorAppointments(Model model) {
        return "doctor-appointments";
    }

    @GetMapping("/doctor/patients")
    public String doctorPatients(Model model) {
        return "doctor-patients";
    }

    @GetMapping("/doctor/schedule")
    public String doctorSchedule(Model model) {
        return "doctor-schedule";
    }

    @GetMapping("/doctor/admitted")
    public String doctorAdmitted(Model model) {
        return "doctor-admitted";
    }

    @GetMapping("/doctor/records")
    public String doctorRecords(Model model) {
        return "doctor-records";
    }

    @GetMapping("/doctor/prescriptions")
    public String doctorPrescriptions(Model model) {
        return "doctor-prescriptions";
    }

    @GetMapping("/doctor/profile")
    public String doctorProfilePage(Model model) {
        return "doctor-my-profile";
    }

    @GetMapping("/doctor/change-password")
    public String doctorChangePassword(Model model) {
        return "doctor-change-password";
    }

    // ========== Department Pages ==========

    @GetMapping("/departments")
    public String departments(Model model) {
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
        return "book-appointment";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        return "appointments";
    }

    // ========== Patient Pages ==========

    @GetMapping("/patients")
    public String patients(Model model) {
        return "patients";
    }

    @GetMapping("/patient/dashboard")
    public String patientDashboard(Model model) {
        return "patient-dashboard";
    }

    // ========== Nurse Pages ==========

    @GetMapping("/nurses")
    public String nurses(Model model) {
        return "nurses";
    }

    @GetMapping("/nurse/dashboard")
    public String nurseDashboard(Model model) {
        return "nurse-dashboard";
    }

    // ========== Room Pages ==========

    @GetMapping("/rooms")
    public String rooms(Model model) {
        return "rooms";
    }

    // ========== Shift Pages ==========

    @GetMapping("/shifts")
    public String shifts(Model model) {
        return "shifts";
    }
}
