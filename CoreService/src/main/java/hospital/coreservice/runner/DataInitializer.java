package hospital.coreservice.runner;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.permission.PermissionCreateDto;
import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.user.UserCreateDto;
import hospital.coreservice.model.enums.*;
import hospital.coreservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final RoomService roomService;
    private final ShiftService shiftService;
    private final DoctorScheduleService doctorScheduleService;
    private final AppointmentService appointmentService;

    @Override
    // @Transactional را حذف کردیم تا هر متد سرویس تراکنش خود را مدیریت کند
    public void run(String... args) {
        try {
            // بررسی اینکه آیا داده‌ها از قبل وجود دارند یا خیر
            if (departmentService.countTotalDepartments() > 0) {
                log.info("⏭️ Data already exists, skipping initialization.");
                return;
            }

            log.info("🚀 Starting data initialization...");

            initRolesAndPermissions();
            initUsers();
            initDepartments();
            initDoctors();
            initNurses();
            initPatients();
            initRooms();
            initShifts();
            initDoctorSchedules();
            initAppointments();

            log.info("✅ All sample data created successfully!");
            log.info("📊 Summary:");
            log.info("   • Users: {}", userService.countAllUsers());
            log.info("   • Departments: {}", departmentService.countTotalDepartments());
            log.info("   • Doctors: {}", doctorService.countAllDoctors());
            log.info("   • Nurses: {}", nurseService.countAllNurses());
            log.info("   • Patients: {}", patientService.countAllPatients());
            log.info("   • Rooms: {}", roomService.countAllRooms());
            log.info("   • Shifts: {}", shiftService.countAllShifts());
            log.info("   • Appointments: {}", appointmentService.countTotalAppointments());

        } catch (Exception e) {
            log.error("❌ Data initialization failed with exception: ", e);
            // در صورت نیاز می‌توانید استثنا را دوباره پرتاب کنید تا برنامه متوقف شود
            // throw new RuntimeException("Data initialization failed", e);
        }
    }

    // ============ 1. Roles & Permissions ============

    private void initRolesAndPermissions() {
        log.info("📋 Creating roles and permissions...");

        if (permissionService.countAllPermissions() == 0) {
            createPermission("CREATE_USER", "Create new users", "USER", "CREATE");
            createPermission("READ_USER", "Read user details", "USER", "READ");
            createPermission("UPDATE_USER", "Update user details", "USER", "UPDATE");
            createPermission("DELETE_USER", "Delete users", "USER", "DELETE");
            createPermission("VIEW_PROFILE", "View user profiles", "USER", "READ");
            createPermission("MANAGE_ROLES", "Manage roles", "ADMIN", "MANAGE");
            createPermission("VIEW_AUDIT", "View audit logs", "ADMIN", "READ");
        }

        if (roleService.countAllRoles() == 0) {
            createRole(RoleName.SUPER_ADMIN, "Super Administrator - Full access");
            createRole(RoleName.ADMIN, "Administrator - Manage users and roles");
            createRole(RoleName.DOCTOR, "Doctor - Medical staff");
            createRole(RoleName.NURSE, "Nurse - Nursing staff");
            createRole(RoleName.PATIENT, "Patient - Regular user");
            createRole(RoleName.RECEPTIONIST, "Receptionist - Front desk");
        }

        log.info("✅ Roles and permissions created successfully");
    }

    private void createPermission(String name, String description, String resource, String action) {
        try {
            permissionService.createPermission(new PermissionCreateDto(name, description, resource, action));
            log.debug("Permission '{}' created", name);
        } catch (Exception e) {
            log.warn("Permission '{}' creation failed: {}", name, e.getMessage(), e);
        }
    }

    private void createRole(RoleName roleName, String description) {
        try {
            roleService.createRole(new hospital.coreservice.dto.role.RoleCreateDto(roleName, description, null));
        } catch (Exception e) {
            log.warn("Role '{}' creation failed: {}", roleName, e.getMessage(), e);
        }
    }

    // ============ 2. Users ============

    private void initUsers() {
        log.info("👤 Creating users...");

        createUser("Dr. John", "Doe", "dr.john", "john.doe@hospital.com", "Doctor@123", "09121111111");
        createUser("Dr. Jane", "Smith", "dr.jane", "jane.smith@hospital.com", "Doctor@123", "09122222222");
        createUser("Nurse Alice", "Brown", "nurse.alice", "alice.brown@hospital.com", "Nurse@123", "09123333333");
        createUser("Nurse Bob", "White", "nurse.bob", "bob.white@hospital.com", "Nurse@123", "09124444444");
        createUser("Patient Jack", "Black", "patient.jack", "jack.black@hospital.com", "Patient@123", "09125555555");
        createUser("Patient Mary", "Green", "patient.mary", "mary.green@hospital.com", "Patient@123", "09126666666");
        createUser("Admin", "System", "admin", "admin@hospital.com", "Admin@123", "09127777777");

        log.info("✅ Users created successfully");
    }

    private void createUser(String firstName, String lastName, String username, String email, String password, String phone) {
        try {
            UserCreateDto dto = new UserCreateDto();
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setUsername(username);
            dto.setEmail(email);
            dto.setPassword(password);
            dto.setPhoneNumber(phone);
            dto.setBirthDate(LocalDate.of(1990, 1, 1));
            userService.registerUser(dto);
            log.debug("User '{}' created", username);
        } catch (Exception e) {
            log.warn("User '{}' creation failed: {}", username, e.getMessage(), e);
        }
    }

    // ============ 3. Departments ============

    private void initDepartments() {
        log.info("🏥 Creating departments...");

        createDepartment("Cardiology", "CARD-001", "Heart diseases and treatments", "Floor 3, Building A", "0211111111");
        createDepartment("Neurology", "NEUR-001", "Brain and nervous system", "Floor 4, Building A", "0212222222");

        log.info("✅ Departments created successfully");
    }

    private void createDepartment(String name, String code, String desc, String location, String phone) {
        try {
            DepartmentCreateDto dto = new DepartmentCreateDto();
            dto.setDepartmentName(name);
            dto.setDepartmentCode(code);
            dto.setDescription(desc);
            dto.setLocation(location);
            dto.setPhoneNumber(phone);
            departmentService.createDepartment(dto);
            log.debug("Department '{}' created", name);
        } catch (Exception e) {
            log.warn("Department '{}' creation failed: {}", name, e.getMessage(), e);
        }
    }

    // ============ 4. Doctors ============

    private void initDoctors() {
        log.info("👨‍⚕️ Creating doctors...");

        createDoctor("dr.john", "Dr. John Doe", "LIC-001", Speciality.CARDIOLOGY,
                List.of(SubSpeciality.INTERVENTIONAL_CARDIOLOGY, SubSpeciality.ECHOCARDIOGRAPHY),
                12, 150000L, "09121111111", 20, 30, "CARD-001");

        createDoctor("dr.jane", "Dr. Jane Smith", "LIC-002", Speciality.NEUROLOGY,
                List.of(SubSpeciality.STROKE, SubSpeciality.EPILEPSY),
                8, 180000L, "09122222222", 15, 30, "NEUR-001");

        log.info("✅ Doctors created successfully");
    }

    private void createDoctor(String username, String fullName, String license, Speciality speciality,
                              List<SubSpeciality> subSpecialities, int experience, Long fee,
                              String phone, int maxAppointments, int slotDuration, String departmentCode) {
        try {
            var user = userService.getUserEntityByUsername(username);
            var department = departmentService.getDepartmentByCode(departmentCode);

            DoctorCreateDto dto = new DoctorCreateDto();
            dto.setUserId(user.getId());
            dto.setFirstName(fullName.split(" ")[0]);
            dto.setLastName(fullName.split(" ")[1]);
            dto.setSpeciality(speciality);
            dto.setSubSpecialities(subSpecialities);
            dto.setLicenseNumber(license);
            dto.setYearsOfExperience(experience);
            dto.setConsultationFee(fee);
            dto.setPhoneNumber(phone);
            dto.setMaxAppointmentsPerDay(maxAppointments);
            dto.setDefaultSlotDuration(slotDuration);
            dto.setDepartmentId(department.getId());

            doctorService.createDoctor(dto);
            log.debug("Doctor '{}' created", username);
        } catch (Exception e) {
            log.warn("Doctor '{}' creation failed: {}", username, e.getMessage(), e);
        }
    }

    // ============ 5. Nurses ============

    private void initNurses() {
        log.info("👩‍⚕️ Creating nurses...");

        createNurse("nurse.alice", "Alice Brown", "NURSE-001", "1234567890",
                NursePosition.HEAD_NURSE, 10, true, "CARD-001");

        createNurse("nurse.bob", "Bob White", "NURSE-002", "0987654321",
                NursePosition.STAFF_NURSE, 5, true, "NEUR-001");

        log.info("✅ Nurses created successfully");
    }

    private void createNurse(String username, String fullName, String nurseCode, String nationalId,
                             NursePosition position, int experience, boolean active, String departmentCode) {
        try {
            var user = userService.getUserEntityByUsername(username);
            var department = departmentService.getDepartmentByCode(departmentCode);

            NurseCreateDto dto = new NurseCreateDto();
            dto.setUserId(user.getId());
            dto.setFirstName(fullName.split(" ")[0]);
            dto.setLastName(fullName.split(" ")[1]);
            dto.setNationalId(nationalId);
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setEmail(user.getEmail());
            dto.setNurseCode(nurseCode);
            dto.setPosition(position);
            dto.setYearsOfExperience(experience);
            dto.setDepartmentIds(Collections.singletonList(department.getId()));

            nurseService.createNurse(dto);
            log.debug("Nurse '{}' created", username);
        } catch (Exception e) {
            log.warn("Nurse '{}' creation failed: {}", username, e.getMessage(), e);
        }
    }

    // ============ 6. Patients ============

    private void initPatients() {
        log.info("🧑‍🤝‍🧑 Creating patients...");

        createPatient("patient.jack", "Jack Black", "1111111111", Gender.MAN,
                BloodType.A_POSITIVE, "123 Main St", "09125555555", "No allergies");

        createPatient("patient.mary", "Mary Green", "2222222222", Gender.FEMALE,
                BloodType.O_POSITIVE, "456 Elm St", "09126666666", "Penicillin allergy");

        log.info("✅ Patients created successfully");
    }

    private void createPatient(String username, String fullName, String nationalId, Gender gender,
                               BloodType bloodType, String address, String phone, String allergies) {
        try {
            var user = userService.getUserEntityByUsername(username);

            PatientCreateDto dto = new PatientCreateDto();
            dto.setNationalId(nationalId);
            dto.setFirstName(fullName.split(" ")[0]);
            dto.setLastName(fullName.split(" ")[1]);
            dto.setGender(gender);
            dto.setPhoneNumber(phone);
            dto.setAddress(address);
            dto.setBloodType(bloodType);
            dto.setStatus(PatientStatus.ACTIVE);
            dto.setAllergies(allergies);
            dto.setBirthDate(LocalDate.of(1980, 5, 15));

            patientService.createPatient(dto);
            log.debug("Patient '{}' created", username);
        } catch (Exception e) {
            log.warn("Patient '{}' creation failed: {}", username, e.getMessage(), e);
        }
    }

    // ============ 7. Rooms ============

    private void initRooms() {
        log.info("🛏️ Creating rooms...");

        createRoom("101", "CARD-001", 4, "ICU with monitors", true);
        createRoom("102", "CARD-001", 2, "Private room", true);
        createRoom("201", "NEUR-001", 3, "Neurology ward", true);

        log.info("✅ Rooms created successfully");
    }

    private void createRoom(String roomNumber, String departmentCode, int capacity, String features, boolean active) {
        try {
            var department = departmentService.getDepartmentByCode(departmentCode);

            RoomCreateDto dto = new RoomCreateDto();
            dto.setRoomNumber(roomNumber);
            dto.setDepartmentId(department.getId());
            dto.setCapacity(capacity);
            dto.setFeatures(features);

            roomService.createRoom(dto);
            log.debug("Room '{}' created", roomNumber);
        } catch (Exception e) {
            log.warn("Room '{}' creation failed: {}", roomNumber, e.getMessage(), e);
        }
    }

    // ============ 8. Shifts ============

    private void initShifts() {
        log.info("🕐 Creating shifts...");

        createShift("Morning Shift", LocalTime.of(6, 0), LocalTime.of(14, 0), 8, false, false);
        createShift("Evening Shift", LocalTime.of(14, 0), LocalTime.of(22, 0), 8, false, false);
        createShift("Night Shift", LocalTime.of(22, 0), LocalTime.of(6, 0), 8, true, true);

        log.info("✅ Shifts created successfully");
    }

    private void createShift(String name, LocalTime start, LocalTime end, int duration, boolean night, boolean extraPay) {
        try {
            ShiftCreateDto dto = new ShiftCreateDto();
            dto.setName(name);
            dto.setStartTime(start);
            dto.setEndTime(end);
            dto.setDurationHours(duration);
            dto.setNightShift(night);
            dto.setHasExtraPay(extraPay);

            shiftService.createShift(dto);
            log.debug("Shift '{}' created", name);
        } catch (Exception e) {
            log.warn("Shift '{}' creation failed: {}", name, e.getMessage(), e);
        }
    }

    // ============ 9. Doctor Schedules ============

    private void initDoctorSchedules() {
        log.info("📅 Creating doctor schedules...");

        createDoctorSchedule("dr.john", DayOfWeek.SATURDAY, LocalTime.of(8, 0), LocalTime.of(14, 0), 30, "Room 101");
        createDoctorSchedule("dr.john", DayOfWeek.SUNDAY, LocalTime.of(8, 0), LocalTime.of(14, 0), 30, "Room 101");
        createDoctorSchedule("dr.john", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(14, 0), 30, "Room 101");
        createDoctorSchedule("dr.john", DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(14, 0), 30, "Room 101");
        createDoctorSchedule("dr.john", DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(14, 0), 30, "Room 101");

        createDoctorSchedule("dr.jane", DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(15, 0), 30, "Room 201");
        createDoctorSchedule("dr.jane", DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(15, 0), 30, "Room 201");
        createDoctorSchedule("dr.jane", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(15, 0), 30, "Room 201");
        createDoctorSchedule("dr.jane", DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(15, 0), 30, "Room 201");

        log.info("✅ Doctor schedules created successfully");
    }

    private void createDoctorSchedule(String username, DayOfWeek day, LocalTime start, LocalTime end, int slotDuration, String location) {
        try {
            var user = userService.getUserEntityByUsername(username);
            var doctor = doctorService.getDoctorByUserId(user.getId());

            DoctorScheduleCreateDto dto = new DoctorScheduleCreateDto();
            dto.setDoctorId(doctor.getId());
            dto.setDayOfWeek(day);
            dto.setStartTime(start);
            dto.setEndTime(end);
            dto.setSlotDuration(slotDuration);
            dto.setLocation(location);

            doctorScheduleService.createDoctorSchedule(dto);
            log.debug("Schedule for '{}' on {} created", username, day);
        } catch (Exception e) {
            log.warn("Schedule for '{}' on {} creation failed: {}", username, day, e.getMessage(), e);
        }
    }

    // ============ 10. Appointments ============

    private void initAppointments() {
        log.info("📋 Creating appointments...");

        createAppointment("patient.jack", "dr.john", "CARD-001", "1111111111",
                LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentType.IN_PERSON, "Chest pain, follow-up");

        createAppointment("patient.mary", "dr.jane", "NEUR-001", "2222222222",
                LocalDate.now().plusDays(2), LocalTime.of(10, 0), LocalTime.of(10, 30),
                AppointmentType.IN_PERSON, "Headache and dizziness");

        log.info("✅ Appointments created successfully");
    }

    private void createAppointment(String patientUsername, String doctorUsername, String departmentCode,
                                   String patientNationalId, LocalDate date, LocalTime start, LocalTime end,
                                   AppointmentType type, String reason) {
        try {
            var doctorUser = userService.getUserEntityByUsername(doctorUsername);
            var doctor = doctorService.getDoctorByUserId(doctorUser.getId());
            var patient = patientService.getPatientByNationalId(patientNationalId);
            var department = departmentService.getDepartmentByCode(departmentCode);

            AppointmentCreateDto dto = new AppointmentCreateDto();
            dto.setPatientId(patient.getId());
            dto.setDoctorId(doctor.getId());
            dto.setDepartmentId(department.getId());
            dto.setAppointmentDate(date);
            dto.setStartTime(start);
            dto.setEndTime(end);
            dto.setType(type);
            dto.setReason(reason);

            appointmentService.createAppointment(dto);
            log.debug("Appointment created for {} with {} on {}", patientUsername, doctorUsername, date);
        } catch (Exception e) {
            log.warn("Appointment creation failed for {} with {} on {}: {}", patientUsername, doctorUsername, date, e.getMessage(), e);
        }
    }
}