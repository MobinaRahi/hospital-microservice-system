package hospital.billingservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * User profile DTO received from AuthService.
 * Used by BillingService to load user details for authentication.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long tenantId;
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<String> roles;
}
