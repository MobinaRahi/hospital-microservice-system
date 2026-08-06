package hospital.inventoryservice.security.service;

import hospital.inventoryservice.client.AuthServiceClient;
import hospital.inventoryservice.dto.auth.UserProfileDto;
import hospital.inventoryservice.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom UserDetailsService for InventoryService.
 * Loads user details from AuthService via Feign client.
 *
 * <p><strong>Multi-Tenancy:</strong></p>
 * User profile includes tenantId for data isolation.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthServiceClient authServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);

        try {
            UserProfileDto userProfile = authServiceClient.getUserByUsername(username);

            if (userProfile == null) {
                log.warn("User not found: {}", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            List<String> roles = userProfile.getRoles() != null
                    ? new ArrayList<>(userProfile.getRoles())
                    : new ArrayList<>();

            SecurityUser securityUser = SecurityUser.create(
                    userProfile.getId(),
                    userProfile.getUsername(),
                    "", // Password is not needed for JWT authentication
                    userProfile.getEmail(),
                    userProfile.getFirstName(),
                    userProfile.getLastName(),
                    userProfile.getTenantId(),
                    userProfile.isEnabled(),
                    roles
            );

            log.debug("User loaded successfully: {} with roles: {}", username, roles);
            return securityUser;

        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to load user details for: {}", username, e);
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }
}