package hospital.coreservice.mapper;

import hospital.coreservice.dto.role.RoleCreateDto;
import hospital.coreservice.dto.role.RoleResponseDto;
import hospital.coreservice.dto.role.RoleSlimResponseDto;
import hospital.coreservice.dto.role.RoleUpdateDto;
import hospital.coreservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
/**
 * MapStruct mapper for Role entity ↔ DTO conversion.
 *
 * @author Mobina
 */
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