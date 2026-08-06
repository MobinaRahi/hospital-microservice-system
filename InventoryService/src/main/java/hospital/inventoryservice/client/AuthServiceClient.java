package hospital.inventoryservice.client;

import hospital.inventoryservice.dto.auth.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for AuthService.
 * Used to load user details for authentication in InventoryService.
 *
 * <p><strong>Endpoints used:</strong></p>
 * <ul>
 *   <li>GET /api/v1/internal/users/username/{username} - Load user by username</li>
 * </ul>
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
     * Used by CustomUserDetailsService during authentication.
     *
     * @param username the username to search for
     * @return the user profile
     */
    @GetMapping("/api/v1/internal/users/username/{username}")
    UserProfileDto getUserByUsername(@PathVariable("username") String username);
}
