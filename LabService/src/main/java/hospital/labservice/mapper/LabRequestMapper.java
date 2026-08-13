package hospital.labservice.mapper;

import hospital.labservice.dto.labrequest.LabRequestCreateDto;
import hospital.labservice.dto.labrequest.LabRequestResponseDto;
import hospital.labservice.dto.labrequest.LabRequestUpdateDto;
import hospital.labservice.model.LabRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabRequest entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps requestNumber, patientId, doctorId, encounterId,
 *       requestDate, priority, clinicalNotes; items are mapped with LabRequestItemMapper</li>
 *   <li>Entity → ResponseDto: Maps all fields + nested items list</li>
 *   <li>UpdateDto → Entity: Partial update (only priority and clinicalNotes)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {LabRequestItemMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabRequestMapper {

    /**
     * Converts LabRequestCreateDto to LabRequest entity.
     * Status defaults to PENDING (set by entity).
     * RequestedBy is set by service layer.
     * Items are mapped via LabRequestItemMapper (without labRequest reference).
     *
     * @param dto the create DTO
     * @return LabRequest entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "items", source = "items")
    @Mapping(target = "tenantId", ignore = true)
    LabRequest toEntity(LabRequestCreateDto dto);

    /**
     * Converts LabRequest entity to LabRequestResponseDto.
     * Includes nested list of LabRequestItemResponseDto.
     *
     * @param entity the LabRequest entity
     * @return LabRequestResponseDto
     */
    @Mapping(target = "items", source = "items")
    LabRequestResponseDto toResponseDto(LabRequest entity);

    /**
     * Converts list of LabRequest entities to list of DTOs.
     *
     * @param entities list of LabRequest entities
     * @return list of LabRequestResponseDto
     */
    List<LabRequestResponseDto> toResponseDtoList(List<LabRequest> entities);

    /**
     * Updates existing LabRequest entity from UpdateDto.
     * Only priority and clinicalNotes can be updated.
     * Null values in DTO are ignored.
     * Identity fields (requestNumber, patientId, doctorId) and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestNumber", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "requestDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(LabRequestUpdateDto dto, @MappingTarget LabRequest entity);
}
