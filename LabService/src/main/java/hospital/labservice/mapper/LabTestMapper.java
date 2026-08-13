package hospital.labservice.mapper;

import hospital.labservice.dto.labtest.LabTestCreateDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.dto.labtest.LabTestUpdateDto;
import hospital.labservice.model.LabTest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabTest entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps all fields (code, name, category, price, etc.)</li>
 *   <li>Entity → ResponseDto: Maps all fields including isActive, createdAt, updatedAt</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored, id and code not updated)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabTestMapper {

    /**
     * Converts LabTestCreateDto to LabTest entity.
     * Fields like isActive (default true) are set by entity defaults.
     *
     * @param dto the create DTO
     * @return LabTest entity
     */
    LabTest toEntity(LabTestCreateDto dto);

    /**
     * Converts LabTest entity to LabTestResponseDto.
     *
     * @param entity the LabTest entity
     * @return LabTestResponseDto
     */
    LabTestResponseDto toResponseDto(LabTest entity);

    /**
     * Converts list of LabTest entities to list of DTOs.
     *
     * @param entities list of LabTest entities
     * @return list of LabTestResponseDto
     */
    List<LabTestResponseDto> toResponseDtoList(List<LabTest> entities);

    /**
     * Updates existing LabTest entity from UpdateDto.
     * Null values in DTO are ignored.
     * Fields id, code, tenantId, and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(LabTestUpdateDto dto, @MappingTarget LabTest entity);
}
