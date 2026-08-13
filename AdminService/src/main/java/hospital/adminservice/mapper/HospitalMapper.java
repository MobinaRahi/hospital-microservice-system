package hospital.adminservice.mapper;

import hospital.adminservice.dto.hospital.HospitalCreateDto;
import hospital.adminservice.dto.hospital.HospitalResponseDto;
import hospital.adminservice.dto.hospital.HospitalUpdateDto;
import hospital.adminservice.model.Hospital;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Hospital entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps all create fields</li>
 *   <li>Entity → ResponseDto: Maps all response fields</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HospitalMapper {

    /**
     * Converts HospitalCreateDto to Hospital entity.
     *
     * @param dto the create DTO
     * @return Hospital entity
     */
    Hospital toEntity(HospitalCreateDto dto);

    /**
     * Converts Hospital entity to HospitalResponseDto.
     *
     * @param entity the Hospital entity
     * @return HospitalResponseDto
     */
    HospitalResponseDto toResponseDto(Hospital entity);

    /**
     * Converts list of Hospital entities to list of DTOs.
     *
     * @param entities list of Hospital entities
     * @return list of HospitalResponseDto
     */
    List<HospitalResponseDto> toResponseDtoList(List<Hospital> entities);

    /**
     * Updates existing Hospital entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(HospitalUpdateDto dto, @MappingTarget Hospital entity);
}
