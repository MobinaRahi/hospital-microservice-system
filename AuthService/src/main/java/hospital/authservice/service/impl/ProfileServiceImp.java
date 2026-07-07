package hospital.authservice.service.impl;

import hospital.authservice.dto.user.UserProfileDto;
import hospital.authservice.mapper.ProfileMapper;
import hospital.authservice.model.User;
import hospital.authservice.repository.UserRepository;
import hospital.authservice.service.ProfileService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProfileServiceImp implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public ProfileServiceImp(UserRepository userRepository,
                             ProfileMapper profileMapper) {
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }

    @Cacheable(value = "userProfiles", key = "#userId", unless = "#result == null")
    public UserProfileDto getUserProfile(@NonNull Long userId) {
        log.debug("🔍 Fetching full profile for user id: {}", userId);

        User user = userRepository.findActiveByIdWithRolesAndPermissions(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        log.debug("✅ Profile fetched successfully for user: {}", user.getUsername());
        return profileMapper.toProfileDto(user);
    }

    @Cacheable(value = "userProfiles", key = "#username", unless = "#result == null")
    public UserProfileDto getUserProfileByUsername(@NonNull String username) {
        log.debug("🔍 Fetching full profile for username: {}", username);

        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        log.debug("✅ Profile fetched successfully for user: {}", user.getUsername());
        return profileMapper.toProfileDto(user);
    }

    public UserProfileDto getCurrentUserProfile(@NonNull String username) {
        log.debug("👤 Fetching current user profile for: {}", username);
        return getUserProfileByUsername(username);
    }

    @CacheEvict(value = "userProfiles", key = "#userId")
    public void evictProfileCache(@NonNull Long userId) {
        log.debug("🗑️ Evicting profile cache for user id: {}", userId);
    }
}
