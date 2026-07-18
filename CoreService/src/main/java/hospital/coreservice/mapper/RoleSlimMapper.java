package hospital.coreservice.mapper;

import hospital.coreservice.dto.role.RoleSlimResponseDto;
import hospital.coreservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
/**
 * MapStruct mapper for Role entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface RoleSlimMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    RoleSlimResponseDto toSlimDto(Role role);

    Set<RoleSlimResponseDto> toSlimDtoSet(Set<Role> roles);
}