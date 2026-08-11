package hospital.billingservice.client;

import hospital.billingservice.dto.auth.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for AuthService.
 * Used to load user details for authentication in BillingService.
 *
 * @author MobinaRahi
 */
@FeignClient(
        name = "auth-service",
        url = "${services.auth.base-url:http://localhost:8281}"
)
public interface AuthServiceClient {

    /**
     * Loads user profile by username from AuthService.
     */
    @GetMapping("/api/v1/internal/users/username/{username}")
    UserProfileDto getUserByUsername(@PathVariable("username") String username);
}
