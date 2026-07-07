package hospital.authservice.service;

import hospital.authservice.dto.user.UserProfileDto;
import lombok.NonNull;

public interface ProfileService {

    UserProfileDto getUserProfile(@NonNull Long userId);

    UserProfileDto getUserProfileByUsername(@NonNull String username);


    UserProfileDto getCurrentUserProfile(@NonNull String username);

    void evictProfileCache(@NonNull Long userId);
}