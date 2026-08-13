package hospital.labservice.mapper;

import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemResponseDto;
import hospital.labservice.model.LabRequestItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabRequestItem entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps testName; test entity is resolved by service layer</li>
 *   <li>Entity → ResponseDto: Maps all fields + nested LabTest and LabResult DTOs</li>
 *   <li>testId is derived from entity.test.id</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {LabTestMapper.class, LabResultMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabRequestItemMapper {

    /**
     * Converts LabRequestItemCreateDto to LabRequestItem entity.
     * The test entity is resolved by the service layer (not mapped from DTO).
     * The labRequest reference is set by the service layer.
     *
     * @param dto the create DTO
     * @return LabRequestItem entity
     */
    @Mapping(target = "labRequest", ignore = true)
    @Mapping(target = "test", ignore = true)
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LabRequestItem toEntity(LabRequestItemCreateDto dto);

    /**
     * Converts LabRequestItem entity to LabRequestItemResponseDto.
     * Maps nested test → LabTestResponseDto and result → LabResultResponseDto.
     *
     * @param entity the LabRequestItem entity
     * @return LabRequestItemResponseDto
     */
    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "test", source = "test")
    @Mapping(target = "result", source = "result")
    LabRequestItemResponseDto toResponseDto(LabRequestItem entity);

    /**
     * Converts list of LabRequestItem entities to list of DTOs.
     *
     * @param entities list of LabRequestItem entities
     * @return list of LabRequestItemResponseDto
     */
    List<LabRequestItemResponseDto> toResponseDtoList(List<LabRequestItem> entities);
}
