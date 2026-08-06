package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.supplier.SupplierCreateDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierUpdateDto;
import hospital.inventoryservice.model.Supplier;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Supplier entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: all fields map directly</li>
 *   <li>Entity → ResponseDto: all fields map directly</li>
 *   <li>UpdateDto → Entity: partial update using null-value ignore strategy</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {

    /**
     * Maps a CreateDto to a Supplier entity.
     * <p>All fields are directly mapped.</p>
     */
    @Mapping(target = "tenantId", ignore = true)
    Supplier toEntity(SupplierCreateDto dto);

    /**
     * Maps a Supplier entity to a ResponseDto.
     * <p>All fields are directly mapped.</p>
     */
    SupplierResponseDto toResponseDto(Supplier entity);

    /**
     * Maps a list of Supplier entities to ResponseDto list.
     */
    List<SupplierResponseDto> toResponseDtoList(List<Supplier> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing Supplier entity.
     * <p>Uses null-value ignore strategy — only provided fields are updated.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(SupplierUpdateDto dto, @MappingTarget Supplier entity);
}
