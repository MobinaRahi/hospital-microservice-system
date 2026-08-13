package hospital.adminservice.mapper;

import hospital.adminservice.dto.bed.BedCreateDto;
import hospital.adminservice.dto.bed.BedResponseDto;
import hospital.adminservice.dto.bed.BedUpdateDto;
import hospital.adminservice.model.Bed;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Bed entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps bedNumber, departmentId, type, notes</li>
 *   <li>Entity → ResponseDto: Maps all fields + computes available flag</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BedMapper {

    /**
     * Converts BedCreateDto to Bed entity.
     *
     * @param dto the create DTO
     * @return Bed entity
     */
    Bed toEntity(BedCreateDto dto);

    /**
     * Converts Bed entity to BedResponseDto.
     *
     * @param entity the Bed entity
     * @return BedResponseDto
     */
    BedResponseDto toResponseDto(Bed entity);

    /**
     * Converts list of Bed entities to list of DTOs.
     *
     * @param entities list of Bed entities
     * @return list of BedResponseDto
     */
    List<BedResponseDto> toResponseDtoList(List<Bed> entities);

    /**
     * Updates existing Bed entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(BedUpdateDto dto, @MappingTarget Bed entity);
}
