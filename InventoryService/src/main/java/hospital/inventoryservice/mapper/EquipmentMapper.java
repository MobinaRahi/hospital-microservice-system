package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.equipment.EquipmentCreateDto;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import hospital.inventoryservice.dto.equipment.EquipmentUpdateDto;
import hospital.inventoryservice.model.Equipment;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Equipment entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: all fields map directly</li>
 *   <li>Entity → ResponseDto: all fields map directly + computed isWarrantyExpired</li>
 *   <li>UpdateDto → Entity: partial update using null-value ignore strategy</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipmentMapper {

    /**
     * Maps a CreateDto to an Equipment entity.
     * <p>All fields are directly mapped.</p>
     */
    @Mapping(target = "status", constant = "AVAILABLE")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "tenantId", ignore = true)
    Equipment toEntity(EquipmentCreateDto dto);

    /**
     * Maps an Equipment entity to a ResponseDto.
     * <p>Includes computed field isWarrantyExpired.</p>
     */
    @Mapping(target = "isWarrantyExpired", expression = "java(entity.isWarrantyExpired())")
    EquipmentResponseDto toResponseDto(Equipment entity);

    /**
     * Maps a list of Equipment entities to ResponseDto list.
     */
    List<EquipmentResponseDto> toResponseDtoList(List<Equipment> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing Equipment entity.
     * <p>Uses null-value ignore strategy — only provided fields are updated.</p>
     * <p>serialNumber, type, purchaseDate are immutable after creation.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "purchaseDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(EquipmentUpdateDto dto, @MappingTarget Equipment entity);
}
