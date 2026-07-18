package hospital.coreservice.mapper;

import hospital.coreservice.dto.role.RoleSlimResponseDto;
import hospital.coreservice.dto.user.UserProfileDto;
import hospital.coreservice.model.Permission;
import hospital.coreservice.model.Role;
import hospital.coreservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoleSlimMapper.class}
)
/**
 * MapStruct mapper for Profile entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface ProfileMapper {

    // ========== Main Mapping ==========

    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    @Mapping(target = "roles", expression = "java(mapRolesToSlim(user.getRoles()))")
    @Mapping(target = "allPermissions", expression = "java(mapAllPermissions(user))")
    @Mapping(target = "daysSinceLastLogin", expression = "java(calculateDaysSinceLastLogin(user))")
    @Mapping(target = "passwordExpired", expression = "java(isPasswordExpired(user))")
    @Mapping(target = "failedAttempts", source = "failedAttempts")
    @Mapping(target = "accountNonLocked", source = "accountNonLocked")
    @Mapping(target = "lastPasswordChange", source = "passwordChangedAt")
    UserProfileDto toProfileDto(User user);

    // ========== Helper Methods ==========

    @Named("getFullName")
    default String getFullName(User user) {
        if (user == null) return null;
        if (user.getFirstName() == null && user.getLastName() == null) return user.getUsername();
        if (user.getFirstName() == null) return user.getLastName();
        if (user.getLastName() == null) return user.getFirstName();
        return user.getFirstName() + " " + user.getLastName();
    }

    @Named("mapRolesToSlim")
    default Set<RoleSlimResponseDto> mapRolesToSlim(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(role -> {
                    RoleSlimResponseDto dto = new RoleSlimResponseDto();
                    dto.setId(role.getId());
                    dto.setName(role.getName());
                    dto.setDescription(role.getDescription());
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    @Named("mapAllPermissions")
    default Set<String> mapAllPermissions(User user) {
        if (user == null || user.getRoles() == null) return null;
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    @Named("calculateDaysSinceLastLogin")
    default long calculateDaysSinceLastLogin(User user) {
        if (user == null || user.getLastLogin() == null) return -1;
        return ChronoUnit.DAYS.between(user.getLastLogin(), LocalDateTime.now());
    }

    @Named("isPasswordExpired")
    default boolean isPasswordExpired(User user) {
        if (user == null || user.getPasswordChangedAt() == null) return true;
        // رمز عبور هر 90 روز یکبار منقضی می‌شود
        return user.getPasswordChangedAt().plusDays(90).isBefore(LocalDateTime.now());
    }
}