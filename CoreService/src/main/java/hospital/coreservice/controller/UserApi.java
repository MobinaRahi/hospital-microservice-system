package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.response.PasswordChangeRequest;
import hospital.coreservice.dto.user.*;
import hospital.coreservice.model.enums.RoleName;
import hospital.coreservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User, profile, role assignment and password APIs")
public class UserApi {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userService.registerUser(createDto), "User registered successfully", HttpStatus.CREATED.value()));
    }

    @PostMapping("/register-with-roles")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerWithRoles(@Valid @RequestBody UserCreateDto createDto, @RequestParam Set<RoleName> roles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userService.registerUserWithRoles(createDto, roles), "User registered with roles", HttpStatus.CREATED.value()));
    }

    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<UserResponseDto>> getByUsername(@RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserByUsername(username), "User found", HttpStatus.OK.value()));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserByEmail(email), "User found", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> profileById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserProfileById(id), "Profile loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/profile/by-username")
    public ResponseEntity<ApiResponse<UserProfileDto>> profileByUsername(@RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserProfileByUsername(username), "Profile loaded", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(id, updateDto), "User updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<UserResponseDto>> assignRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        return ResponseEntity.ok(ApiResponse.success(userService.assignRolesToUser(id, roleIds), "Roles assigned", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/roles/{roleId}/add")
    public ResponseEntity<ApiResponse<UserResponseDto>> addRole(@PathVariable Long id, @PathVariable Long roleId) {
        return ResponseEntity.ok(ApiResponse.success(userService.addRoleToUser(id, roleId), "Role added", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/roles/{roleId}/remove")
    public ResponseEntity<ApiResponse<UserResponseDto>> removeRole(@PathVariable Long id, @PathVariable Long roleId) {
        return ResponseEntity.ok(ApiResponse.success(userService.removeRoleFromUser(id, roleId), "Role removed", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ApiResponse<UserResponseDto>> enable(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.enableUser(id), "User enabled", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<UserResponseDto>> disable(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.disableUser(id), "User disabled", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<UserResponseDto>> unlock(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.unlockUser(id), "User unlocked", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/verify-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> verifyEmail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.verifyEmail(id), "Email verified", HttpStatus.OK.value()));
    }

    @GetMapping("/disabled")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> disabledUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getDisabledUsers(), "Disabled users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/locked")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> lockedUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getLockedUsers(), "Locked users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> inactiveUsers(@RequestParam LocalDateTime date) {
        return ResponseEntity.ok(ApiResponse.success(userService.getInactiveUsers(date), "Inactive users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/unverified")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> unverifiedUsers(@RequestParam LocalDateTime before) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUnverifiedUsersOlderThan(before), "Unverified users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/by-role")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> byRole(@RequestParam String roleName) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsersByRoleName(roleName), "Users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/by-permission")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> byPermission(@RequestParam String permissionName) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsersWithPermission(permissionName), "Users loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> search(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(keyword, pageable), "Users found", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> active(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllActiveUsers(pageable), "Active users loaded", HttpStatus.OK.value()));
    }

    // ==================== Summaries ====================

    @GetMapping("/summaries")
    public ResponseEntity<ApiResponse<List<UserSlimResponseDto>>> summaries() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUserSummaries(), "User summaries loaded", HttpStatus.OK.value()));
    }

    // ==================== Counts ====================

    @GetMapping("/count/active")
    public ResponseEntity<ApiResponse<Long>> countActive() {
        return ResponseEntity.ok(ApiResponse.success(userService.countActiveUsers(), "Active users count", HttpStatus.OK.value()));
    }

    @GetMapping("/count/enabled")
    public ResponseEntity<ApiResponse<Long>> countEnabled() {
        return ResponseEntity.ok(ApiResponse.success(userService.countEnabledUsers(), "Enabled users count", HttpStatus.OK.value()));
    }

    @GetMapping("/count/by-role")
    public ResponseEntity<ApiResponse<Long>> countByRole(@RequestParam String roleName) {
        return ResponseEntity.ok(ApiResponse.success(userService.countUsersByRole(roleName), "Users by role count", HttpStatus.OK.value()));
    }

    // ==================== Bulk Operations ====================

    @PatchMapping("/bulk/disable-inactive")
    public ResponseEntity<ApiResponse<Integer>> disableInactive(@RequestParam LocalDateTime cutoffDate) {
        return ResponseEntity.ok(ApiResponse.success(userService.disableInactiveUsers(cutoffDate), "Inactive users disabled", HttpStatus.OK.value()));
    }

    @DeleteMapping("/bulk/soft-delete")
    public ResponseEntity<ApiResponse<Void>> softDeleteBulk(@RequestBody List<Long> ids) {
        userService.softDeleteUsersBulk(ids);
        return ResponseEntity.ok(ApiResponse.success("Users soft deleted", HttpStatus.OK.value()));
    }

    // ==================== Existence Checks ====================

    @GetMapping("/exists/username")
    public ResponseEntity<ApiResponse<Boolean>> existsByUsername(@RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.existsByUsername(username), "Username existence checked", HttpStatus.OK.value()));
    }

    @GetMapping("/exists/email")
    public ResponseEntity<ApiResponse<Boolean>> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.success(userService.existsByEmail(email), "Email existence checked", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/password-expired")
    public ResponseEntity<ApiResponse<Boolean>> passwordExpired(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.isPasswordExpired(id), "Password expiration checked", HttpStatus.OK.value()));
    }
}

