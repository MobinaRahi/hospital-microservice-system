package hospital.labservice.mapper;

import hospital.labservice.dto.sample.SampleCreateDto;
import hospital.labservice.dto.sample.SampleResponseDto;
import hospital.labservice.dto.sample.SampleUpdateDto;
import hospital.labservice.model.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Sample entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps sampleNumber, sampleType, collectionDate, collectedBy,
 *       collectionSite, containerType, quality, notes; labRequest entity is resolved by service layer</li>
 *   <li>Entity → ResponseDto: Maps all fields; labRequestId derived from labRequest.id</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SampleMapper {

    /**
     * Converts SampleCreateDto to Sample entity.
     * The labRequest entity is resolved by the service layer (not mapped from DTO).
     * receivedAtLab and receivedBy are set by the entity's markReceived() business method.
     *
     * @param dto the create DTO
     * @return Sample entity
     */
    @Mapping(target = "labRequest", ignore = true)
    @Mapping(target = "receivedAtLab", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Sample toEntity(SampleCreateDto dto);

    /**
     * Converts Sample entity to SampleResponseDto.
     * labRequestId is derived from the nested labRequest.id.
     *
     * @param entity the Sample entity
     * @return SampleResponseDto
     */
    @Mapping(target = "labRequestId", source = "labRequest.id")
    SampleResponseDto toResponseDto(Sample entity);

    /**
     * Converts list of Sample entities to list of DTOs.
     *
     * @param entities list of Sample entities
     * @return list of SampleResponseDto
     */
    List<SampleResponseDto> toResponseDtoList(List<Sample> entities);

    /**
     * Updates existing Sample entity from UpdateDto.
     * Null values in DTO are ignored.
     * Identity fields (labRequestId, sampleNumber) and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "labRequest", ignore = true)
    @Mapping(target = "sampleNumber", ignore = true)
    @Mapping(target = "sampleType", ignore = true)
    @Mapping(target = "collectionDate", ignore = true)
    @Mapping(target = "collectedBy", ignore = true)
    @Mapping(target = "receivedAtLab", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(SampleUpdateDto dto, @MappingTarget Sample entity);
}
