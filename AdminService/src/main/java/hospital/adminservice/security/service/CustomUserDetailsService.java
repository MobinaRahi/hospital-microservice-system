package hospital.adminservice.security.service;

import hospital.adminservice.client.AuthServiceClient;
import hospital.adminservice.dto.auth.UserProfileDto;
import hospital.adminservice.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            if (userProfile == null) throw new UsernameNotFoundException("User not found: " + username);

            List<String> roles = userProfile.getRoles() != null
                    ? new ArrayList<>(userProfile.getRoles()) : new ArrayList<>();

            return SecurityUser.create(
                    userProfile.getId(), userProfile.getUsername(), "",
                    userProfile.getEmail(), userProfile.getFirstName(), userProfile.getLastName(),
                    userProfile.getTenantId(), userProfile.isEnabled(), roles);
        } catch (UsernameNotFoundException e) { throw e; }
        catch (Exception e) {
            log.error("Failed to load user details for: {}", username, e);
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }
}
