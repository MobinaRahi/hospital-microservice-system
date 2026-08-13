package hospital.labservice.mapper;

import hospital.labservice.dto.labresult.LabResultCreateDto;
import hospital.labservice.dto.labresult.LabResultResponseDto;
import hospital.labservice.model.LabResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabResult entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps value, normalRange, flag, unit, performedAt, performedBy, notes;
 *       requestItem entity is resolved by service layer</li>
 *   <li>Entity → ResponseDto: Maps all fields; requestItemId derived from requestItem.id</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabResultMapper {

    /**
     * Converts LabResultCreateDto to LabResult entity.
     * The requestItem entity is resolved by the service layer (not mapped from DTO).
     * VerifiedAt and verifiedBy are set by the entity's verify() business method.
     *
     * @param dto the create DTO
     * @return LabResult entity
     */
    @Mapping(target = "requestItem", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LabResult toEntity(LabResultCreateDto dto);

    /**
     * Converts LabResult entity to LabResultResponseDto.
     * requestItemId is derived from the nested requestItem.id.
     *
     * @param entity the LabResult entity
     * @return LabResultResponseDto
     */
    @Mapping(target = "requestItemId", source = "requestItem.id")
    LabResultResponseDto toResponseDto(LabResult entity);

    /**
     * Converts list of LabResult entities to list of DTOs.
     *
     * @param entities list of LabResult entities
     * @return list of LabResultResponseDto
     */
    List<LabResultResponseDto> toResponseDtoList(List<LabResult> entities);
}
