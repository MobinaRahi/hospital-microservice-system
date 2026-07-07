package hospital.authservice.controller;

import hospital.authservice.dto.internal.InternalUserProfileDto;
import hospital.authservice.dto.response.ApiResponse;
import hospital.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserApi {

    private final UserService userService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<InternalUserProfileDto>> profileById(@PathVariable Long id) {
        InternalUserProfileDto profile = userService.getInternalProfileById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        profile,
                        "Internal user profile loaded",
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<InternalUserProfileDto>> profileByUsername(@RequestParam String username) {
        InternalUserProfileDto profile = userService.getInternalProfileByUsername(username);

        return ResponseEntity.ok(
                ApiResponse.success(
                        profile,
                        "Internal user profile loaded",
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/{id}/has-role")
    public ResponseEntity<ApiResponse<Boolean>> hasRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        boolean result = userService.userHasRole(id, role);

        return ResponseEntity.ok(
                ApiResponse.success(
                        result,
                        "User role checked",
                        HttpStatus.OK.value()
                )
        );
    }
}