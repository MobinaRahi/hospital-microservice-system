package hospital.authservice.dto.internal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Getter
@Setter
public class InternalUserProfileDto {
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String nationalId;

    private boolean enabled;

    private boolean accountNonLocked;

    private Set<String> roles;
}