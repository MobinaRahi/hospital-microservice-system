package hospital.authservice.runner;

import hospital.authservice.dto.permission.PermissionCreateDto;
import hospital.authservice.dto.role.RoleCreateDto;
import hospital.authservice.dto.user.UserCreateDto;
import hospital.authservice.model.enums.RoleName;
import hospital.authservice.service.PermissionService;
import hospital.authservice.service.RoleService;
import hospital.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class AuthDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public void run(String... args) {
        try {
            initRolesAndPermissions();
            initUsers();
        } catch (Exception e) {
            log.error("AuthService data initialization failed", e);
        }
    }

    private void initRolesAndPermissions() {
        if (permissionService.countAllPermissions() == 0) {
            createPermission("CREATE_USER", "ایجاد کاربر جدید", "USER", "CREATE");
            createPermission("READ_USER", "مشاهده اطلاعات کاربر", "USER", "READ");
            createPermission("UPDATE_USER", "ویرایش اطلاعات کاربر", "USER", "UPDATE");
            createPermission("DELETE_USER", "حذف کاربر", "USER", "DELETE");
            createPermission("VIEW_PROFILE", "مشاهده پروفایل کاربران", "USER", "READ");
            createPermission("MANAGE_ROLES", "مدیریت نقش‌ها", "ADMIN", "MANAGE");
            createPermission("VIEW_AUDIT", "مشاهده لاگ‌های حسابرسی", "ADMIN", "READ");
        }

        if (roleService.countAllRoles() == 0) {
            createRole(RoleName.SUPER_ADMIN, "مدیر ارشد سیستم - دسترسی کامل");
            createRole(RoleName.ADMIN, "مدیر سیستم - مدیریت کاربران و نقش‌ها");
            createRole(RoleName.DOCTOR, "پزشک - کادر پزشکی");
            createRole(RoleName.NURSE, "پرستار - کادر پرستاری");
            createRole(RoleName.PATIENT, "بیمار - کاربر عادی");
            createRole(RoleName.RECEPTIONIST, "پذیرش - میز جلو");
            createRole(RoleName.PHARMACIST, "داروساز");
            createRole(RoleName.LAB_TECHNICIAN, "تکنسین آزمایشگاه");
            createRole(RoleName.ACCOUNTANT, "حسابدار");
            createRole(RoleName.RADIOLOGIST, "رادیولوژیست");
        }
    }

    private void initUsers() {
        if (userService.countAllUsers() > 0) {
            log.info("Users already exist. Skipping user initialization.");
            return;
        }

        createUser("دکتر علی", "رضایی", "dr.ali", "ali.rezaei@hospital.com", "Doctor@123", "09121111111", Set.of(RoleName.DOCTOR));
        createUser("دکتر مریم", "احمدی", "dr.maryam", "maryam.ahmadi@hospital.com", "Doctor@123", "09122222222", Set.of(RoleName.DOCTOR));
        createUser("پرستار فاطمه", "محمدی", "nurse.fatemeh", "fatemeh.mohammadi@hospital.com", "Nurse@123", "09123333333", Set.of(RoleName.NURSE));
        createUser("پرستار حسین", "کریمی", "nurse.hossein", "hossein.karimi@hospital.com", "Nurse@123", "09124444444", Set.of(RoleName.NURSE));
        createUser("بیمار رضا", "نجفی", "patient.reza", "reza.najafi@hospital.com", "Patient@123", "09125555555", Set.of(RoleName.PATIENT));
        createUser("بیمار زهرا", "حسینی", "patient.zahra", "zahra.hosseini@hospital.com", "Patient@123", "09126666666", Set.of(RoleName.PATIENT));
        createUser("مدیر", "سیستم", "admin", "admin@hospital.com", "Admin@123", "09127777777", Set.of(RoleName.SUPER_ADMIN, RoleName.ADMIN));
    }

    private void createPermission(String name, String description, String resource, String action) {
        try {
            permissionService.createPermission(
                    new PermissionCreateDto(name, description, resource, action)
            );
        } catch (Exception e) {
            log.warn("Permission '{}' creation skipped: {}", name, e.getMessage());
        }
    }

    private void createRole(RoleName roleName, String description) {
        try {
            roleService.createRole(
                    new RoleCreateDto(roleName, description, null)
            );
        } catch (Exception e) {
            log.warn("Role '{}' creation skipped: {}", roleName, e.getMessage());
        }
    }

    private void createUser(
            String firstName,
            String lastName,
            String username,
            String email,
            String password,
            String phone,
            Set<RoleName> roles
    ) {
        try {
            UserCreateDto dto = new UserCreateDto();
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setUsername(username);
            dto.setEmail(email);
            dto.setPassword(password);
            dto.setPhoneNumber(phone);
            dto.setBirthDate(LocalDate.of(1990, 1, 1));

            userService.registerUserWithRoles(dto, roles);
        } catch (Exception e) {
            log.warn("User '{}' creation skipped: {}", username, e.getMessage());
        }
    }
}