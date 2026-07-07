package hospital.authservice.controller;

import hospital.authservice.dto.response.ApiResponse;
import hospital.authservice.dto.user.UserProfileDto;
import hospital.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserApi {

    private final UserService userService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> profileById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserProfileById(id), "Internal user profile loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<UserProfileDto>> profileByUsername(@RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserProfileByUsername(username), "Internal user profile loaded", HttpStatus.OK.value()));
    }
}
