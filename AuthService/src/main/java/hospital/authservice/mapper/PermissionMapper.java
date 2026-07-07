package hospital.authservice.mapper;

import hospital.authservice.dto.permission.PermissionCreateDto;
import hospital.authservice.dto.permission.PermissionResponseDto;
import hospital.authservice.dto.permission.PermissionUpdateDto;
import hospital.authservice.model.Permission;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PermissionMapper {

    // ========== Create ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "resource", source = "resource")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "deleted", constant = "false")
    Permission toEntity(PermissionCreateDto createDto);

    // ========== Update ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(@MappingTarget Permission permission, PermissionUpdateDto updateDto);

    // ========== Response ==========
    @Mapping(target = "roleCount", expression = "java(permission.getRoles() != null ? permission.getRoles().size() : 0)")
    PermissionResponseDto toResponseDto(Permission permission);

}