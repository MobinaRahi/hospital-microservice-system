package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.user.UserProfileDto;
import hospital.coreservice.security.model.SecurityUser;
import hospital.coreservice.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Current user profile APIs")
public class ProfileApi {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> me(@AuthenticationPrincipal SecurityUser currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication is required", HttpStatus.UNAUTHORIZED.value()));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getCurrentUserProfile(currentUser.getUsername()),
                "Profile loaded",
                HttpStatus.OK.value()
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileDto>> byId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getUserProfile(userId),
                "Profile loaded",
                HttpStatus.OK.value()
        ));
    }

    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<UserProfileDto>> byUsername(@RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getUserProfileByUsername(username),
                "Profile loaded",
                HttpStatus.OK.value()
        ));
    }

    @DeleteMapping("/{userId}/cache")
    public ResponseEntity<ApiResponse<Void>> evict(@PathVariable Long userId) {
        profileService.evictProfileCache(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile cache evicted", HttpStatus.OK.value()));
    }
}