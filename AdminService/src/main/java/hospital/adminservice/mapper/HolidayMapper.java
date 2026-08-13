package hospital.adminservice.mapper;

import hospital.adminservice.dto.holiday.HolidayCreateDto;
import hospital.adminservice.dto.holiday.HolidayResponseDto;
import hospital.adminservice.dto.holiday.HolidayUpdateDto;
import hospital.adminservice.model.Holiday;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Holiday entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps name, date, year, flags</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 *   <li>UpdateDto → Entity: Partial update (name, flags only)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HolidayMapper {

    /**
     * Converts HolidayCreateDto to Holiday entity.
     *
     * @param dto the create DTO
     * @return Holiday entity
     */
    Holiday toEntity(HolidayCreateDto dto);

    /**
     * Converts Holiday entity to HolidayResponseDto.
     *
     * @param entity the Holiday entity
     * @return HolidayResponseDto
     */
    HolidayResponseDto toResponseDto(Holiday entity);

    /**
     * Converts list of Holiday entities to list of DTOs.
     *
     * @param entities list of Holiday entities
     * @return list of HolidayResponseDto
     */
    List<HolidayResponseDto> toResponseDtoList(List<Holiday> entities);

    /**
     * Updates existing Holiday entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(HolidayUpdateDto dto, @MappingTarget Holiday entity);
}
