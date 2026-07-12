package hospital.coreservice.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
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
    private boolean emailVerified;
    private boolean twoFactorEnabled;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}