package hospital.labservice.mapper;

import hospital.labservice.dto.labequipment.LabEquipmentCreateDto;
import hospital.labservice.dto.labequipment.LabEquipmentResponseDto;
import hospital.labservice.dto.labequipment.LabEquipmentUpdateDto;
import hospital.labservice.model.LabEquipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabEquipment entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps name, model, manufacturer, serialNumber, status,
 *       calibration dates, location, notes</li>
 *   <li>Entity → ResponseDto: Maps all fields including createdAt, updatedAt</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored, identity fields not updated)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabEquipmentMapper {

    /**
     * Converts LabEquipmentCreateDto to LabEquipment entity.
     * Status defaults to OPERATIONAL if not specified (set by entity).
     *
     * @param dto the create DTO
     * @return LabEquipment entity
     */
    LabEquipment toEntity(LabEquipmentCreateDto dto);

    /**
     * Converts LabEquipment entity to LabEquipmentResponseDto.
     *
     * @param entity the LabEquipment entity
     * @return LabEquipmentResponseDto
     */
    LabEquipmentResponseDto toResponseDto(LabEquipment entity);

    /**
     * Converts list of LabEquipment entities to list of DTOs.
     *
     * @param entities list of LabEquipment entities
     * @return list of LabEquipmentResponseDto
     */
    List<LabEquipmentResponseDto> toResponseDtoList(List<LabEquipment> entities);

    /**
     * Updates existing LabEquipment entity from UpdateDto.
     * Null values in DTO are ignored.
     * Identity fields (name, serialNumber) and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(LabEquipmentUpdateDto dto, @MappingTarget LabEquipment entity);
}
