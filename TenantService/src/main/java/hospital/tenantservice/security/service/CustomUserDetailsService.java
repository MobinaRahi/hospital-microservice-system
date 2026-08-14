package hospital.tenantservice.security.service;

import hospital.tenantservice.client.AuthServiceClient;
import hospital.tenantservice.dto.auth.UserProfileDto;
import hospital.tenantservice.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom UserDetailsService that loads user from AuthService via Feign client.
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
        try {
            UserProfileDto userProfile = authServiceClient.getUserByUsername(username);

            return SecurityUser.builder()
                    .id(userProfile.getId())
                    .username(userProfile.getUsername())
                    .password("")
                    .tenantId(userProfile.getTenantId())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        } catch (Exception ex) {
            log.error("Failed to load user: {}", username, ex);
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
