package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.stock.StockCreateDto;
import hospital.inventoryservice.dto.stock.StockResponseDto;
import hospital.inventoryservice.dto.stock.StockUpdateDto;
import hospital.inventoryservice.model.Stock;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Stock entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: drugId is ignored — service resolves the Drug entity</li>
 *   <li>Entity → ResponseDto: drug is mapped as nested DrugResponseDto</li>
 *   <li>Computed fields (isExpired, isLowStock) are set manually in service</li>
 *   <li>UpdateDto → Entity: partial update (only quantity, levels, location)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    /**
     * Maps a CreateDto to a Stock entity.
     * <p>drugId is ignored — service layer will resolve and set the Drug entity.</p>
     */
    @Mapping(target = "drug", ignore = true)
    @Mapping(target = "lastRestockedAt", ignore = true)
    @Mapping(target = "lastRestockedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Stock toEntity(StockCreateDto dto);

    /**
     * Maps a Stock entity to a ResponseDto.
     * <p>Includes nested drug information.</p>
     * <p>Computed fields (isExpired, isLowStock) must be set manually after mapping.</p>
     */
    @Mapping(target = "drug", source = "drug")
    @Mapping(target = "isExpired", expression = "java(entity.isExpired())")
    @Mapping(target = "isLowStock", expression = "java(entity.isLowStock())")
    StockResponseDto toResponseDto(Stock entity);

    /**
     * Maps a list of Stock entities to ResponseDto list.
     */
    List<StockResponseDto> toResponseDtoList(List<Stock> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing Stock entity.
     * <p>Only quantity, minStockLevel, maxStockLevel, and location can be updated.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drug", ignore = true)
    @Mapping(target = "batchNumber", ignore = true)
    @Mapping(target = "expiryDate", ignore = true)
    @Mapping(target = "lastRestockedAt", ignore = true)
    @Mapping(target = "lastRestockedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(StockUpdateDto dto, @MappingTarget Stock entity);
}
