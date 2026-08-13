package hospital.adminservice.mapper;

import hospital.adminservice.dto.shift.ShiftCreateDto;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import hospital.adminservice.dto.shift.ShiftUpdateDto;
import hospital.adminservice.model.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Shift entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps name, code, times, duration, flags</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShiftMapper {

    /**
     * Converts ShiftCreateDto to Shift entity.
     *
     * @param dto the create DTO
     * @return Shift entity
     */
    Shift toEntity(ShiftCreateDto dto);

    /**
     * Converts Shift entity to ShiftResponseDto.
     *
     * @param entity the Shift entity
     * @return ShiftResponseDto
     */
    ShiftResponseDto toResponseDto(Shift entity);

    /**
     * Converts list of Shift entities to list of DTOs.
     *
     * @param entities list of Shift entities
     * @return list of ShiftResponseDto
     */
    List<ShiftResponseDto> toResponseDtoList(List<Shift> entities);

    /**
     * Updates existing Shift entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(ShiftUpdateDto dto, @MappingTarget Shift entity);
}
