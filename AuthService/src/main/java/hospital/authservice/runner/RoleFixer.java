package hospital.authservice.runner;

import hospital.authservice.model.Role;
import hospital.authservice.model.User;
import hospital.authservice.model.enums.RoleName;
import hospital.authservice.repository.RoleRepository;
import hospital.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class RoleFixer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("🔍 بررسی و اصلاح نقش‌های گم‌شده کاربران...");

        fixUserRole("admin", RoleName.ADMIN);
        fixUserRole("admin", RoleName.SUPER_ADMIN);
        fixUserRole("dr.ali", RoleName.DOCTOR);
        fixUserRole("dr.maryam", RoleName.DOCTOR);
        fixUserRole("nurse.fatemeh", RoleName.NURSE);
        fixUserRole("nurse.hossein", RoleName.NURSE);
        fixUserRole("patient.reza", RoleName.PATIENT);
        fixUserRole("patient.zahra", RoleName.PATIENT);

        log.info("✅ اصلاح نقش‌ها به پایان رسید.");
    }

    private void fixUserRole(String username, RoleName roleName) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean hasRole = user.getRoles().stream()
                    .anyMatch(r -> r.getName() == roleName);

            if (!hasRole) {
                Optional<Role> roleOpt = roleRepository.findByName(roleName);
                if (roleOpt.isPresent()) {
                    user.getRoles().add(roleOpt.get());
                    userRepository.save(user);
                    log.info("🛠️ نقش {} به کاربر {} اضافه شد.", roleName, username);
                } else {
                    log.warn("⚠️ نقش {} در دیتابیس یافت نشد!", roleName);
                }
            }
        }
    }
}
