package hospital.tenantservice.client;

import hospital.tenantservice.dto.auth.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for AuthService.
 *
 * <p>Used to load user details for authentication in TenantService.</p>
 *
 * @author MobinaRahi
 */
@FeignClient(name = "auth-service", url = "${services.auth.base-url:http://localhost:8281}")
public interface AuthServiceClient {

    /**
     * Loads user profile by username from AuthService.
     *
     * @param username the username to search for
     * @return UserProfileDto with user details
     */
    @GetMapping("/api/v1/internal/users/username/{username}")
    UserProfileDto getUserByUsername(@PathVariable("username") String username);
}
