package hospital.adminservice.mapper;

import hospital.adminservice.dto.systemlog.SystemLogCreateDto;
import hospital.adminservice.dto.systemlog.SystemLogResponseDto;
import hospital.adminservice.model.SystemLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for SystemLog entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SystemLog toEntity(SystemLogCreateDto dto);

    @Mapping(target = "isSevere", expression = "java(entity.isSevere())")
    SystemLogResponseDto toResponseDto(SystemLog entity);

    List<SystemLogResponseDto> toResponseDtoList(List<SystemLog> entities);
}
