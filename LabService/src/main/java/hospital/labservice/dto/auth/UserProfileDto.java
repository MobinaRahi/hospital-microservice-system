package hospital.labservice.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * User profile DTO received from AuthService.
 * Used by LabService to load user details for authentication.
 *
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>id - User ID from AuthService</li>
 *   <li>username - Username</li>
 *   <li>email - Email address</li>
 *   <li>firstName - First name</li>
 *   <li>lastName - Last name</li>
 *   <li>phoneNumber - Phone number</li>
 *   <li>tenantId - Tenant ID for multi-tenancy</li>
 *   <li>enabled - Whether user is enabled</li>
 *   <li>accountNonLocked - Whether account is not locked</li>
 *   <li>roles - Set of role names</li>
 * </ul>
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
