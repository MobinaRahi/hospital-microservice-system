package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.drug.DrugCreateDto;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import hospital.inventoryservice.dto.drug.DrugUpdateDto;
import hospital.inventoryservice.model.Drug;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Drug entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: categoryId is ignored — service resolves the DrugCategory entity</li>
 *   <li>Entity → ResponseDto: category is mapped as nested DrugCategoryResponseDto</li>
 *   <li>UpdateDto → Entity: partial update using null-value ignore strategy</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DrugMapper {

    /**
     * Maps a CreateDto to a Drug entity.
     * <p>categoryId is ignored — service layer will resolve and set the DrugCategory entity.</p>
     */
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Drug toEntity(DrugCreateDto dto);

    /**
     * Maps a Drug entity to a ResponseDto.
     * <p>Includes nested category information.</p>
     */
    @Mapping(target = "category", source = "category")
    DrugResponseDto toResponseDto(Drug entity);

    /**
     * Maps a list of Drug entities to ResponseDto list.
     */
    List<DrugResponseDto> toResponseDtoList(List<Drug> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing Drug entity.
     * <p>Uses null-value ignore strategy — only provided fields are updated.</p>
     * <p>genericName, category are not updatable (immutable after creation).</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genericName", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(DrugUpdateDto dto, @MappingTarget Drug entity);
}
