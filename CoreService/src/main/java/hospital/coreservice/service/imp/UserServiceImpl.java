package hospital.coreservice.service.imp;

import hospital.coreservice.dto.response.PasswordChangeRequest;
import hospital.coreservice.dto.role.RoleSlimResponseDto;
import hospital.coreservice.dto.user.*;
import hospital.coreservice.exception.role.RoleNotFoundException;
import hospital.coreservice.exception.user.DuplicateEmailException;
import hospital.coreservice.exception.user.DuplicateUsernameException;
import hospital.coreservice.exception.user.InvalidPasswordException;
import hospital.coreservice.exception.user.UserNotFoundException;
import hospital.coreservice.mapper.UserMapper;
import hospital.coreservice.model.Permission;
import hospital.coreservice.model.Role;
import hospital.coreservice.model.User;
import hospital.coreservice.model.enums.RoleName;
import hospital.coreservice.repository.RoleRepository;
import hospital.coreservice.repository.UserRepository;
import hospital.coreservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    // ── Security constants ──────────────────────────────────────────────────
    private static final int  MAX_FAILED_ATTEMPTS    = 5;
    private static final int  LOCK_DURATION_MINUTES  = 30;
    private static final int  MAX_PASSWORD_AGE_DAYS  = 90;

    // ── Dependencies ────────────────────────────────────────────────────────
    private final UserRepository  userRepository;
    private final RoleRepository  roleRepository;
    private final UserMapper      userMapper;
    private final PasswordEncoder passwordEncoder;


    // ════════════════════════════════════════════════════════════════════════
    // Registration
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public UserResponseDto registerUser(@NonNull UserCreateDto createDto) {
        log.info("Registering user: {}", createDto.getUsername());

        validateUniqueUsername(createDto.getUsername());
        validateUniqueEmail(createDto.getEmail());

        User user = buildUserFromDto(createDto);
        User saved = userRepository.save(user);

        log.info("User registered with id: {}", saved.getId());
        return userMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public UserResponseDto registerUserWithRoles(@NonNull UserCreateDto createDto,
                                                 @NonNull Set<RoleName> roleNames) {
        log.info("Registering user '{}' with roles: {}", createDto.getUsername(), roleNames);

        validateUniqueUsername(createDto.getUsername());
        validateUniqueEmail(createDto.getEmail());

        User user = buildUserFromDto(createDto);

        Set<Role> roles = roleNames.stream()
                .map(rn -> roleRepository.findByName(rn)
                        .orElseThrow(() -> new RoleNotFoundException(Long.valueOf(rn.name()))))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        User saved = userRepository.save(user);
        log.info("User registered with id: {} and roles: {}", saved.getId(), roleNames);
        return userMapper.toResponseDto(saved);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Read – single record
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByUsername(@NonNull String username) {
        log.debug("Fetching user by username: {}", username);
        return userMapper.toResponseDto(findActiveByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(@NonNull String email) {
        log.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByUsername(@NonNull String username) {
        return findActiveByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityById(@NonNull Long id) {
        return findActiveById(id);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Profile (with roles + permissions)
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileById(@NonNull Long userId) {
        log.debug("Fetching profile for user id: {}", userId);
        User user = userRepository.findActiveByIdWithRolesAndPermissions(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));
        return buildProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileByUsername(@NonNull String username) {
        log.debug("Fetching profile for username: {}", username);
        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));
        return buildProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(@NonNull String username) {
        return getUserProfileByUsername(username);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public UserResponseDto updateUser(@NonNull Long userId, @NonNull UserUpdateDto updateDto) {
        log.info("Updating user id: {}", userId);

        User user = findActiveById(userId);

        // If email is changing → uniqueness check
        if (updateDto.getEmail() != null && !updateDto.getEmail().equalsIgnoreCase(user.getEmail())) {
            validateUniqueEmail(updateDto.getEmail());
        }

        userMapper.updateEntity(user, updateDto);
        User saved = userRepository.save(user);

        log.info("User updated id: {}", userId);
        return userMapper.toResponseDto(saved);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Password management
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public void changePassword(@NonNull Long userId, @NonNull PasswordChangeRequest passwordDto) {
        log.info("Changing password for user id: {}", userId);

        User user = findActiveById(userId);

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        user.setPasswordHash(passwordEncoder.encode(passwordDto.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password changed for user id: {}", userId);
    }

    @Override
    @Transactional
    public void resetPassword(@NonNull Long userId, @NonNull String newPassword) {
        log.warn("Admin reset password for user id: {}", userId);

        User user = findActiveById(userId);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Role management
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public UserResponseDto assignRolesToUser(@NonNull Long userId, @NonNull Set<Long> roleIds) {
        log.info("Assigning roles {} to user id: {}", roleIds, userId);

        User user = findActiveById(userId);
        user.getRoles().clear();

        Set<Role> roles = roleIds.stream()
                .map(rid -> roleRepository.findById(rid)
                        .orElseThrow(() -> new RoleNotFoundException(rid)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto addRoleToUser(@NonNull Long userId, @NonNull Long roleId) {
        log.info("Adding role {} to user id: {}", roleId, userId);

        User user = findActiveById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        user.addRole(role);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto removeRoleFromUser(@NonNull Long userId, @NonNull Long roleId) {
        log.info("Removing role {} from user id: {}", roleId, userId);

        User user = findActiveById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        user.removeRole(role);

        return userMapper.toResponseDto(userRepository.save(user));
    }


    // ════════════════════════════════════════════════════════════════════════
    // Account status management
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public UserResponseDto enableUser(@NonNull Long userId) {
        log.info("Enabling user id: {}", userId);
        User user = findActiveById(userId);
        user.setEnabled(true);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto disableUser(@NonNull Long userId) {
        log.info("Disabling user id: {}", userId);
        User user = findActiveById(userId);
        user.setEnabled(false);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto unlockUser(@NonNull Long userId) {
        log.info("Unlocking user id: {}", userId);
        User user = findActiveById(userId);
        user.setAccountNonLocked(true);
        user.setLockedUntil(null);
        user.setFailedAttempts(0);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto verifyEmail(@NonNull Long userId) {
        log.info("Verifying email for user id: {}", userId);
        User user = findActiveById(userId);
        user.setEmailVerified(true);
        return userMapper.toResponseDto(userRepository.save(user));
    }


    // ════════════════════════════════════════════════════════════════════════
    // Login tracking
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public void recordSuccessfulLogin(@NonNull String username, @NonNull String ipAddress) {
        log.debug("Recording successful login for: {}", username);
        userRepository.findByUsername(username).ifPresent(user -> {
            user.recordSuccessfulLogin(ipAddress);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void recordFailedLogin(@NonNull String username) {
        log.warn("Recording failed login for: {}", username);
        userRepository.findByUsername(username).ifPresent(user -> {
            user.recordFailedLogin(MAX_FAILED_ATTEMPTS, LOCK_DURATION_MINUTES);
            userRepository.save(user);
        });
    }


    // ════════════════════════════════════════════════════════════════════════
    // Read – lists
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getDisabledUsers() {
        return userRepository.findDisabledUsers().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getLockedUsers() {
        return userRepository.findLockedUsers().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getInactiveUsers(@NonNull LocalDateTime date) {
        return userRepository.findInactiveUsers(date).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUnverifiedUsersOlderThan(@NonNull LocalDateTime dateTime) {
        return userRepository.findUnverifiedUsersOlderThan(dateTime).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByRoleName(@NonNull String roleName) {
        return userRepository.findAllActiveUsersByRoleName(roleName).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithPermission(@NonNull String permissionName) {
        return userRepository.findAllActiveUsersWithPermission(permissionName).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    // ════════════════════════════════════════════════════════════════════════
    // Paginated queries
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(@NonNull String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllActiveUsers(Pageable pageable) {
        return userRepository.findAllByDeletedFalse(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSlimResponseDto> getAllUserSummaries() {
        return userRepository.findAllUserSummaries().stream()
                .map(s -> {
                    UserSlimResponseDto dto = new UserSlimResponseDto();
                    dto.setId(s.getId());
                    dto.setUsername(s.getUsername());
                    dto.setEmail(s.getEmail());
                    String full = (s.getFirstName() != null ? s.getFirstName() : "")
                            + " "
                            + (s.getLastName() != null ? s.getLastName() : "");
                    dto.setFullName(full.trim());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // ════════════════════════════════════════════════════════════════════════
    // Counts
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByDeletedFalseAndEnabledTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long countEnabledUsers() {
        return userRepository.countByDeletedFalseAndEnabledTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRole(@NonNull String roleName) {
        return userRepository.countActiveUsersByRole(roleName);
    }

    @Override
    public Long countAllUsers() {
        return userRepository.count();
    }


    // ════════════════════════════════════════════════════════════════════════
    // Bulk operations
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public int disableInactiveUsers(@NonNull LocalDateTime cutoffDate) {
        log.warn("Disabling inactive users with last login before: {}", cutoffDate);
        int count = userRepository.disableInactiveUsers(cutoffDate);
        log.info("Disabled {} inactive users", count);
        return count;
    }

    @Override
    @Transactional
    public void softDeleteUsersBulk(@NonNull List<Long> userIds) {
        log.warn("Soft-deleting {} users", userIds.size());
        userIds.forEach(id -> {
            User user = findActiveById(id);
            user.setDeleted(true);
            user.setDeletedAt(LocalDateTime.now());
            userRepository.save(user);
        });
        log.info("Soft-deleted {} users", userIds.size());
    }


    // ════════════════════════════════════════════════════════════════════════
    // Existence / uniqueness checks
    // ════════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameUnique(@NonNull String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(@NonNull String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(@NonNull String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(@NonNull String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordExpired(@NonNull Long userId) {
        User user = findActiveById(userId);
        return user.isPasswordExpired(MAX_PASSWORD_AGE_DAYS);
    }


    // ════════════════════════════════════════════════════════════════════════
    // Private helpers
    // ════════════════════════════════════════════════════════════════════════

    private User findActiveById(Long id) {
        return userRepository.findActiveByIdWithRolesAndPermissions(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }

    private User findActiveByUsername(String username) {
        return userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));
    }

    private void validateUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
    }

    /**
     * Builds a {@link User} entity from {@link UserCreateDto},
     * encodes the password, and sets safe defaults.
     */
    private User buildUserFromDto(UserCreateDto createDto) {
        User user = userMapper.toEntity(createDto);
        user.setPasswordHash(passwordEncoder.encode(createDto.getPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        return user;
    }

    /**
     * Converts a {@link User} (with eagerly loaded roles + permissions) to
     * a fully populated {@link UserProfileDto}.
     */
    private UserProfileDto buildProfileDto(User user) {
        UserProfileDto dto = userMapper.toProfileDto(user);

        // roles → slim DTOs
        Set<RoleSlimResponseDto> roleDtoList = user.getRoles().stream()
                .map(r -> {
                    RoleSlimResponseDto rd = new RoleSlimResponseDto();
                    rd.setId(r.getId());
                    rd.setName(r.getName());
                    rd.setDescription(r.getDescription());
                    return rd;
                })
                .collect(Collectors.toSet());
        dto.setRoles(roleDtoList);

        // flatten all permission names
        Set<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
        dto.setAllPermissions(permissions);

        // days since last login
        if (user.getLastLogin() != null) {
            dto.setDaysSinceLastLogin(ChronoUnit.DAYS.between(user.getLastLogin(), LocalDateTime.now()));
        }

        // password expired
        dto.setPasswordExpired(user.isPasswordExpired(MAX_PASSWORD_AGE_DAYS));

        return dto;
    }
}