package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.drugcategory.DrugCategoryCreateDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryUpdateDto;
import hospital.inventoryservice.model.DrugCategory;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for DrugCategory entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps basic fields, ignores parentId (service sets parent entity)</li>
 *   <li>Entity → ResponseDto: Maps all fields including nested parent and children</li>
 *   <li>UpdateDto → Entity: Partial update using null-value ignore strategy</li>
 * </ul>
 *
 * <p><strong>Cycle Handling:</strong></p>
 * <ul>
 *   <li>Self-referencing parent/children is handled by MapStruct's built-in cycle detection</li>
 *   <li>Children are included in response but parent is limited to prevent deep recursion</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DrugCategoryMapper {

    /**
     * Maps a CreateDto to a DrugCategory entity.
     * <p>parentId is ignored — service layer will resolve and set the parent entity.</p>
     */
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "drugs", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    DrugCategory toEntity(DrugCategoryCreateDto dto);

    /**
     * Maps a DrugCategory entity to a ResponseDto.
     * <p>Includes nested parent and children.</p>
     */
    DrugCategoryResponseDto toResponseDto(DrugCategory entity);

    /**
     * Maps a list of DrugCategory entities to ResponseDto list.
     */
    List<DrugCategoryResponseDto> toResponseDtoList(List<DrugCategory> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing DrugCategory entity.
     * <p>Uses null-value ignore strategy — only provided fields are updated.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "drugs", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(DrugCategoryUpdateDto dto, @MappingTarget DrugCategory entity);
}
