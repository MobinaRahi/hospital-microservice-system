package hospital.adminservice.mapper;

import hospital.adminservice.dto.systemconfig.SystemConfigCreateDto;
import hospital.adminservice.dto.systemconfig.SystemConfigResponseDto;
import hospital.adminservice.dto.systemconfig.SystemConfigUpdateDto;
import hospital.adminservice.model.SystemConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for SystemConfiguration entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps configKey, configValue, category, etc.</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 *   <li>UpdateDto → Entity: Partial update (value, category, description, flags)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemConfigMapper {

    /**
     * Converts SystemConfigCreateDto to SystemConfiguration entity.
     *
     * @param dto the create DTO
     * @return SystemConfiguration entity
     */
    SystemConfiguration toEntity(SystemConfigCreateDto dto);

    /**
     * Converts SystemConfiguration entity to SystemConfigResponseDto.
     *
     * @param entity the SystemConfiguration entity
     * @return SystemConfigResponseDto
     */
    SystemConfigResponseDto toResponseDto(SystemConfiguration entity);

    /**
     * Converts list of SystemConfiguration entities to list of DTOs.
     *
     * @param entities list of SystemConfiguration entities
     * @return list of SystemConfigResponseDto
     */
    List<SystemConfigResponseDto> toResponseDtoList(List<SystemConfiguration> entities);

    /**
     * Updates existing SystemConfiguration entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(SystemConfigUpdateDto dto, @MappingTarget SystemConfiguration entity);
}
