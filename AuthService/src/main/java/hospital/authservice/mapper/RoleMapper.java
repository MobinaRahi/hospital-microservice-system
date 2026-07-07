package hospital.authservice.mapper;

import hospital.authservice.dto.role.RoleCreateDto;
import hospital.authservice.dto.role.RoleResponseDto;
import hospital.authservice.dto.role.RoleSlimResponseDto;
import hospital.authservice.dto.role.RoleUpdateDto;
import hospital.authservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Role toEntity(RoleCreateDto createDto);

    @Mapping(target = "active", expression = "java(!role.isDeleted())")
    @Mapping(target = "permissions", source = "permissions")
    @Mapping(target = "userCount", ignore = true)
    RoleResponseDto toResponseDto(Role role);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    RoleSlimResponseDto toSlimResponseDto(Role role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateRoleFromDto(RoleUpdateDto updateDto, @MappingTarget Role role);
}