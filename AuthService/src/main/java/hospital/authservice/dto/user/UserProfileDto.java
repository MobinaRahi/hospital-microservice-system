package hospital.authservice.dto.user;

import hospital.authservice.dto.role.RoleSlimResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String nationalId;
    private LocalDate birthDate;
    private String profilePictureUrl;

    private boolean enabled;
    private boolean accountNonLocked;
    private boolean emailVerified;
    private boolean twoFactorEnabled;
    private LocalDateTime lastLogin;
    private LocalDateTime lastPasswordChange;

    private Set<RoleSlimResponseDto> roles;

    private Set<String> allPermissions;

    private int failedAttempts;
    private long daysSinceLastLogin;
    private boolean passwordExpired;
}