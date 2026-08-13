package hospital.adminservice.client;

import hospital.adminservice.dto.auth.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${services.auth.base-url:http://localhost:8281}")
public interface AuthServiceClient {

    @GetMapping("/api/v1/internal/users/username/{username}")
    UserProfileDto getUserByUsername(@PathVariable("username") String username);
}
