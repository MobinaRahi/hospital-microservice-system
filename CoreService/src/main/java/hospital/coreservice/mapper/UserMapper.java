package hospital.coreservice.mapper;

import hospital.coreservice.dto.role.RoleSlimResponseDto;
import hospital.coreservice.dto.user.*;

import hospital.coreservice.model.Permission;
import hospital.coreservice.model.User;
import hospital.coreservice.model.Role;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoleSlimMapper.class}
)
public interface UserMapper {

    // ========== Create Mapping ==========

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "failedAttempts", constant = "0")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "twoFactorEnabled", constant = "false")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "passwordChangedAt", ignore = true)
    @Mapping(target = "secretKey", ignore = true)
    User toEntity(UserCreateDto createDto);

    // ========== Update Mapping ==========

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "failedAttempts", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "twoFactorEnabled", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "passwordChangedAt", ignore = true)
    @Mapping(target = "secretKey", ignore = true)
    @Mapping(target = "nationalId", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    void updateEntity(@MappingTarget User user, UserUpdateDto updateDto);

    // ========== Response Mapping ==========

    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    UserResponseDto toResponseDto(User user);

    // ========== Slim Response Mapping ==========

    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    UserSlimResponseDto toSlimDto(User user);

    // ========== Profile Mapping ==========

    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    @Mapping(target = "roles", expression = "java(mapRolesToSlim(user.getRoles()))")
    @Mapping(target = "allPermissions", expression = "java(mapAllPermissions(user))")
    @Mapping(target = "daysSinceLastLogin", expression = "java(calculateDaysSinceLastLogin(user))")
    @Mapping(target = "passwordExpired", expression = "java(isPasswordExpired(user))")
    UserProfileDto toProfileDto(User user);

    // ========== Helper Methods (Default = با @Named) ==========

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
        return user.getPasswordChangedAt().plusDays(90).isBefore(LocalDateTime.now());
    }
}