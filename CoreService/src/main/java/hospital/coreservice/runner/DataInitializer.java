package hospital.coreservice.runner;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.model.enums.*;
import hospital.coreservice.client.AuthClient;
import hospital.coreservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final AuthClient authClient;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final RoomService roomService;
    private final ShiftService shiftService;
    private final DoctorScheduleService doctorScheduleService;
    private final AppointmentService appointmentService;


    @Override
    public void run(String... args) {
        try {
            if (departmentService.countTotalDepartments() > 0) {
                log.info("⏭️ داده‌ها از قبل موجود هستند، مقداردهی اولیه نادیده گرفته می‌شود.");
                return;
            }

            log.info("🚀 شروع مقداردهی اولیه داده‌ها...");

            initDepartments();
            initDoctors();
            initNurses();
            initPatients();
            initRooms();
            initShifts();
            initDoctorSchedules();
            initAppointments();

            log.info("✅ تمام داده‌های نمونه با موفقیت ایجاد شدند!");
            log.info("📊 خلاصه:");
            log.info("   • بخش‌ها: {}", departmentService.countTotalDepartments());
            log.info("   • پزشکان: {}", doctorService.countAllDoctors());
            log.info("   • پرستاران: {}", nurseService.countAllNurses());
            log.info("   • بیماران: {}", patientService.countAllPatients());
            log.info("   • اتاق‌ها: {}", roomService.countAllRooms());
            log.info("   • شیفت‌ها: {}", shiftService.countAllShifts());
            log.info("   • نوبت‌ها: {}", appointmentService.countTotalAppointments());

        } catch (Exception e) {
            log.error("❌ مقداردهی اولیه داده‌ها با خطا مواجه شد: ", e);
        }
    }

    // ============ 1. بخش‌ها ============

    private void initDepartments() {
        log.info("🏥 ایجاد بخش‌ها...");

        createDepartment("قلب و عروق", "CARD-001", "بیماری‌ها و درمان‌های قلبی", "طبقه سوم، ساختمان الف", "0211111111");
        createDepartment("مغز و اعصاب", "NEUR-001", "مغز و سیستم عصبی", "طبقه چهارم، ساختمان الف", "0212222222");

        log.info("✅ بخش‌ها با موفقیت ایجاد شدند");
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
            log.debug("بخش '{}' ایجاد شد", name);
        } catch (Exception e) {
            log.warn("ایجاد بخش '{}' ناموفق بود: {}", name, e.getMessage(), e);
        }
    }

    // ============ 4. پزشکان ============

    private void initDoctors() {
        log.info("👨‍⚕️ ایجاد پزشکان...");

        createDoctor("dr.ali", "دکتر سجاد رضایی", "LIC-001", Speciality.CARDIOLOGY,
                List.of(SubSpeciality.INTERVENTIONAL_CARDIOLOGY, SubSpeciality.ECHOCARDIOGRAPHY),
                12, 150000L, "09121111111", 20, 30, "CARD-001");

        createDoctor("dr.maryam", "دکتر محبوبه سخندان", "LIC-002", Speciality.NEUROLOGY,
                List.of(SubSpeciality.STROKE, SubSpeciality.EPILEPSY),
                8, 180000L, "09122222222", 15, 30, "NEUR-001");

        log.info("✅ پزشکان با موفقیت ایجاد شدند");
    }

    private void createDoctor(String username, String fullName, String license, Speciality speciality,
                              List<SubSpeciality> subSpecialities, int experience, Long fee,
                              String phone, int maxAppointments, int slotDuration, String departmentCode) {
        try {
            Long userId = authClient.getUserIdByUsername(username);
            var department = departmentService.getDepartmentByCode(departmentCode);

            DoctorCreateDto dto = new DoctorCreateDto();
            dto.setUserId(userId);
            dto.setFirstName(fullName.split(" ")[1]);
            dto.setLastName(fullName.split(" ")[2]);
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
            log.debug("پزشک '{}' ایجاد شد", username);
        } catch (Exception e) {
            log.warn("ایجاد پزشک '{}' ناموفق بود: {}", username, e.getMessage(), e);
        }
    }

    // ============ 5. پرستاران ============

    private void initNurses() {
        log.info("👩‍⚕️ ایجاد پرستاران...");

        createNurse("nurse.fatemeh", "فاطمه محمدی", "NURSE-001", "1234567890",
                NursePosition.HEAD_NURSE, 10, true, "CARD-001");

        createNurse("nurse.hossein", "حسین کریمی", "NURSE-002", "0987654321",
                NursePosition.STAFF_NURSE, 5, true, "NEUR-001");

        log.info("✅ پرستاران با موفقیت ایجاد شدند");
    }

    private void createNurse(String username, String fullName, String nurseCode, String nationalId,
                             NursePosition position, int experience, boolean active, String departmentCode) {
        try {
            Long userId = authClient.getUserIdByUsername(username);
            var department = departmentService.getDepartmentByCode(departmentCode);

            NurseCreateDto dto = new NurseCreateDto();
            dto.setUserId(userId);
            dto.setFirstName(fullName.split(" ")[0]);
            dto.setLastName(fullName.split(" ")[1]);
            dto.setNationalId(nationalId);
            dto.setPhoneNumber(resolvePhone(username));
            dto.setEmail(resolveEmail(username));
            dto.setNurseCode(nurseCode);
            dto.setPosition(position);
            dto.setYearsOfExperience(experience);
            dto.setDepartmentIds(Collections.singletonList(department.getId()));

            nurseService.createNurse(dto);
            log.debug("پرستار '{}' ایجاد شد", username);
        } catch (Exception e) {
            log.warn("ایجاد پرستار '{}' ناموفق بود: {}", username, e.getMessage(), e);
        }
    }

    // ============ 6. بیماران ============

    private void initPatients() {
        log.info("🧑‍🤝‍🧑 ایجاد بیماران...");

        createPatient("patient.reza", "رضا نجفی", "1111111111", Gender.MAN,
                BloodType.A_POSITIVE, "تهران، خیابان ولیعصر، پلاک ۱۲", "09125555555", "بدون حساسیت خاص");

        createPatient("patient.zahra", "زهرا حسینی", "2222222222", Gender.FEMALE,
                BloodType.O_POSITIVE, "تهران، خیابان انقلاب، پلاک ۴۵", "09126666666", "حساسیت به پنی‌سیلین");

        log.info("✅ بیماران با موفقیت ایجاد شدند");
    }

    private void createPatient(String username, String fullName, String nationalId, Gender gender,
                               BloodType bloodType, String address, String phone, String allergies) {
        try {
            Long userId = authClient.getUserIdByUsername(username);

            PatientCreateDto dto = new PatientCreateDto();
            dto.setUserId(userId);
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
            log.debug("بیمار '{}' ایجاد شد", username);
        } catch (Exception e) {
            log.warn("ایجاد بیمار '{}' ناموفق بود: {}", username, e.getMessage(), e);
        }
    }


    // ============ 7. اتاق‌ها ============

    private void initRooms() {
        log.info("🛏️ ایجاد اتاق‌ها...");

        createRoom("101", "CARD-001", 4, "بخش مراقبت‌های ویژه با مانیتورینگ", true);
        createRoom("102", "CARD-001", 2, "اتاق خصوصی", true);
        createRoom("201", "NEUR-001", 3, "بخش اعصاب", true);

        log.info("✅ اتاق‌ها با موفقیت ایجاد شدند");
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
            log.debug("اتاق '{}' ایجاد شد", roomNumber);
        } catch (Exception e) {
            log.warn("ایجاد اتاق '{}' ناموفق بود: {}", roomNumber, e.getMessage(), e);
        }
    }

    // ============ 8. شیفت‌ها ============

    private void initShifts() {
        log.info("🕐 ایجاد شیفت‌ها...");

        createShift("شیفت صبح", LocalTime.of(6, 0), LocalTime.of(14, 0), 8, false, false);
        createShift("شیفت عصر", LocalTime.of(14, 0), LocalTime.of(22, 0), 8, false, false);
        createShift("شیفت شب", LocalTime.of(22, 0), LocalTime.of(6, 0), 8, true, true);

        log.info("✅ شیفت‌ها با موفقیت ایجاد شدند");
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
            log.debug("شیفت '{}' ایجاد شد", name);
        } catch (Exception e) {
            log.warn("ایجاد شیفت '{}' ناموفق بود: {}", name, e.getMessage(), e);
        }
    }

    // ============ 9. برنامه پزشکان ============

// ============ 9. برنامه پزشکان ============

    private void initDoctorSchedules() {
        log.info("📅 ایجاد برنامه پزشکان...");

        int year = LocalDate.now().getYear(); // سال جاری

        // ===== دکتر علی =====
        createDoctorSchedule("dr.ali", DayOfWeek.SATURDAY,
                LocalDateTime.of(year, 6, 27, 8, 0), LocalDateTime.of(year, 6, 27, 14, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.SUNDAY,
                LocalDateTime.of(year, 6, 28, 8, 0), LocalDateTime.of(year, 6, 28, 14, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.MONDAY,
                LocalDateTime.of(year, 6, 29, 8, 0), LocalDateTime.of(year, 6, 29, 14, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.TUESDAY,
                LocalDateTime.of(year, 6, 30, 8, 0), LocalDateTime.of(year, 6, 30, 14, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.WEDNESDAY,
                LocalDateTime.of(year, 6, 24, 8, 0), LocalDateTime.of(year, 6, 24, 14, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.THURSDAY,
                LocalDateTime.of(year, 6, 25, 8, 0), LocalDateTime.of(year, 6, 25, 12, 0), 30, "اتاق ۱۰۱");
        createDoctorSchedule("dr.ali", DayOfWeek.FRIDAY,
                LocalDateTime.of(year, 6, 26, 8, 0), LocalDateTime.of(year, 6, 26, 12, 0), 30, "اتاق ۱۰۱");

        // ===== دکتر مریم =====
        createDoctorSchedule("dr.maryam", DayOfWeek.SATURDAY,
                LocalDateTime.of(year, 6, 27, 9, 0), LocalDateTime.of(year, 6, 27, 15, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.SUNDAY,
                LocalDateTime.of(year, 6, 28, 9, 0), LocalDateTime.of(year, 6, 28, 15, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.MONDAY,
                LocalDateTime.of(year, 6, 29, 9, 0), LocalDateTime.of(year, 6, 29, 15, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.TUESDAY,
                LocalDateTime.of(year, 6, 30, 9, 0), LocalDateTime.of(year, 6, 30, 15, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.WEDNESDAY,
                LocalDateTime.of(year, 6, 24, 9, 0), LocalDateTime.of(year, 6, 24, 15, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.THURSDAY,
                LocalDateTime.of(year, 6, 25, 9, 0), LocalDateTime.of(year, 6, 25, 13, 0), 30, "اتاق ۲۰۱");
        createDoctorSchedule("dr.maryam", DayOfWeek.FRIDAY,
                LocalDateTime.of(year, 6, 26, 9, 0), LocalDateTime.of(year, 6, 26, 13, 0), 30, "اتاق ۲۰۱");

        log.info("✅ برنامه پزشکان با موفقیت ایجاد شد");
    }

    private void createDoctorSchedule(String username, DayOfWeek day, LocalDateTime start, LocalDateTime end, int slotDuration, String location) {
        try {
            Long userId = resolveUserId(username);
            var doctor = doctorService.getDoctorByUserId(userId);

            DoctorScheduleCreateDto dto = new DoctorScheduleCreateDto();
            dto.setDoctorId(doctor.getId());
            dto.setDayOfWeek(day);
            dto.setStartTime(start);
            dto.setEndTime(end);
            dto.setSlotDuration(slotDuration);
            dto.setLocation(location);

            doctorScheduleService.createDoctorSchedule(dto);
            log.debug("برنامه '{}' برای روز {} ایجاد شد", username, day);
        } catch (Exception e) {
            log.warn("ایجاد برنامه '{}' برای روز {} ناموفق بود: {}", username, day, e.getMessage(), e);
        }
    }

    // ============ 10. نوبت‌ها ============

    private void initAppointments() {
        log.info("📋 ایجاد نوبت‌ها...");

        createAppointment("patient.reza", "dr.ali", "CARD-001", "1111111111",
                LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(9, 30),
                AppointmentType.IN_PERSON, "درد قفسه سینه، ویزیت پیگیری");

        createAppointment("patient.zahra", "dr.maryam", "NEUR-001", "2222222222",
                LocalDate.now().plusDays(2), LocalTime.of(10, 0), LocalTime.of(10, 30),
                AppointmentType.IN_PERSON, "سردرد و سرگیجه");

        log.info("✅ نوبت‌ها با موفقیت ایجاد شدند");
    }

    private void createAppointment(String patientUsername, String doctorUsername, String departmentCode,
                                   String patientNationalId, LocalDate date, LocalTime start, LocalTime end,
                                   AppointmentType type, String reason) {
        try {
            Long doctorUserId = resolveUserId(doctorUsername);
            var doctor = doctorService.getDoctorByUserId(doctorUserId);
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
            log.debug("نوبت برای {} با {} در تاریخ {} ایجاد شد", patientUsername, doctorUsername, date);
        } catch (Exception e) {
            log.warn("ایجاد نوبت برای {} با {} در تاریخ {} ناموفق بود: {}", patientUsername, doctorUsername, date, e.getMessage(), e);
        }
    }

    private Long resolveUserId(String username) {
        Long id = authClient.getUserIdByUsername(username);
        if (id != null) return id;
        Long fallback = demoUserIds().get(username);
        if (fallback != null) {
            log.warn("Using fallback demo userId={} for username='{}'. Start AuthService first for real microservice resolution.", fallback, username);
            return fallback;
        }
        throw new IllegalArgumentException("Cannot resolve userId for username: " + username);
    }

    private String resolvePhone(String username) {
        return switch (username) {
            case "nurse.fatemeh" -> "09123333333";
            case "nurse.hossein" -> "09124444444";
            default -> "09120000000";
        };
    }

    private String resolveEmail(String username) {
        return switch (username) {
            case "nurse.fatemeh" -> "fatemeh.mohammadi@hospital.com";
            case "nurse.hossein" -> "hossein.karimi@hospital.com";
            default -> username + "@hospital.com";
        };
    }

    private Map<String, Long> demoUserIds() {
        return Map.of(
                "dr.ali", 1L,
                "dr.maryam", 2L,
                "nurse.fatemeh", 3L,
                "nurse.hossein", 4L,
                "patient.reza", 5L,
                "patient.zahra", 6L,
                "admin", 7L
        );

    }

}
