package hospital.coreservice.service;

import hospital.coreservice.dto.user.UserProfileDto;
import lombok.NonNull;

/**
 * Service interface for user profile management.
 * Handles profile retrieval and cache.
 *
 * @author Mobina
 */
public interface ProfileService {

    UserProfileDto getUserProfile(@NonNull Long userId);

    UserProfileDto getUserProfileByUsername(@NonNull String username);


    UserProfileDto getCurrentUserProfile(@NonNull String username);

    void evictProfileCache(@NonNull Long userId);
}