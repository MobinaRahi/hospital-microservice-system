package hospital.authservice.mapper;

import hospital.authservice.dto.role.RoleSlimResponseDto;
import hospital.authservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleSlimMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    RoleSlimResponseDto toSlimDto(Role role);

    Set<RoleSlimResponseDto> toSlimDtoSet(Set<Role> roles);
}