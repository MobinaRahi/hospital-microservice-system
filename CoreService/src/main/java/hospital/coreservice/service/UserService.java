package hospital.coreservice.service;

import hospital.coreservice.dto.response.PasswordChangeRequest;
import hospital.coreservice.dto.user.*;
import hospital.coreservice.model.User;
import hospital.coreservice.model.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserService {

    UserResponseDto registerUser(@NonNull UserCreateDto createDto);
    UserResponseDto registerUserWithRoles(@NonNull UserCreateDto createDto, @NonNull Set<RoleName> roleNames);
    UserResponseDto getUserByUsername(@NonNull String username);
    UserResponseDto getUserByEmail(@NonNull String email);
    User getUserEntityByUsername(@NonNull String username);
    User getUserEntityById(@NonNull Long id);
    UserProfileDto getUserProfileById(@NonNull Long userId);
    UserProfileDto getUserProfileByUsername(@NonNull String username);
    UserProfileDto getCurrentUserProfile(@NonNull String username);
    UserResponseDto updateUser(@NonNull Long userId, @NonNull UserUpdateDto updateDto);
    void changePassword(@NonNull Long userId, @NonNull PasswordChangeRequest passwordDto);
    void resetPassword(@NonNull Long userId, @NonNull String newPassword);
    UserResponseDto assignRolesToUser(@NonNull Long userId, @NonNull Set<Long> roleIds);
    UserResponseDto addRoleToUser(@NonNull Long userId, @NonNull Long roleId);
    UserResponseDto removeRoleFromUser(@NonNull Long userId, @NonNull Long roleId);
    UserResponseDto enableUser(@NonNull Long userId);
    UserResponseDto disableUser(@NonNull Long userId);
    UserResponseDto unlockUser(@NonNull Long userId);
    UserResponseDto verifyEmail(@NonNull Long userId);
    void recordSuccessfulLogin(@NonNull String username, @NonNull String ipAddress);
    void recordFailedLogin(@NonNull String username);
    List<UserResponseDto> getDisabledUsers();
    List<UserResponseDto> getLockedUsers();
    List<UserResponseDto> getInactiveUsers(@NonNull LocalDateTime date);
    List<UserResponseDto> getUnverifiedUsersOlderThan(@NonNull LocalDateTime dateTime);
    List<UserResponseDto> getUsersByRoleName(@NonNull String roleName);
    List<UserResponseDto> getUsersWithPermission(@NonNull String permissionName);
    Page<UserResponseDto> searchUsers(@NonNull String keyword, Pageable pageable);
    Page<UserResponseDto> getAllActiveUsers(Pageable pageable);
    List<UserSlimResponseDto> getAllUserSummaries();
    long countActiveUsers();
    long countEnabledUsers();
    long countUsersByRole(@NonNull String roleName);
    Long countAllUsers();
    int disableInactiveUsers(@NonNull LocalDateTime cutoffDate);
    void softDeleteUsersBulk(@NonNull List<Long> userIds);
    boolean isUsernameUnique(@NonNull String username);
    boolean isEmailUnique(@NonNull String email);
    boolean existsByUsername(@NonNull String username);
    boolean existsByEmail(@NonNull String email);
    boolean isPasswordExpired(@NonNull Long userId);
    void changePassword(Long userId, String newPassword, String confirmPassword);
    public void setPassword(Long userId, String newPassword);
}
