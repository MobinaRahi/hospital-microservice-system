package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentCreateDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentResponseDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentUpdateDto;
import hospital.inventoryservice.model.EquipmentAssignment;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for EquipmentAssignment entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: equipmentId is ignored — service resolves Equipment entity</li>
 *   <li>Entity → ResponseDto: equipment is mapped as nested EquipmentResponseDto</li>
 *   <li>isActive is computed via entity method</li>
 *   <li>UpdateDto → Entity: partial update (return dates, notes)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {EquipmentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipmentAssignmentMapper {

    /**
     * Maps a CreateDto to an EquipmentAssignment entity.
     * <p>equipmentId is ignored — service layer will resolve and set the Equipment entity.</p>
     * <p>assignedBy is set by service layer from SecurityContext.</p>
     */
    @Mapping(target = "equipment", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    EquipmentAssignment toEntity(EquipmentAssignmentCreateDto dto);

    /**
     * Maps an EquipmentAssignment entity to a ResponseDto.
     * <p>Includes nested equipment information and computed isActive.</p>
     */
    @Mapping(target = "equipment", source = "equipment")
    @Mapping(target = "isActive", expression = "java(entity.isActive())")
    EquipmentAssignmentResponseDto toResponseDto(EquipmentAssignment entity);

    /**
     * Maps a list of EquipmentAssignment entities to ResponseDto list.
     */
    List<EquipmentAssignmentResponseDto> toResponseDtoList(List<EquipmentAssignment> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing EquipmentAssignment entity.
     * <p>Only expectedReturnDate, actualReturnDate, and notes can be updated.</p>
     * <p>equipment, patientId, departmentId, assignedDate are immutable after creation.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipment", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "assignedDate", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(EquipmentAssignmentUpdateDto dto, @MappingTarget EquipmentAssignment entity);
}
