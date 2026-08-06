package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderCreateDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderResponseDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderUpdateDto;
import hospital.inventoryservice.model.PurchaseOrder;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for PurchaseOrder entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: supplierId is ignored — service resolves the Supplier entity</li>
 *   <li>Items are mapped through PurchaseOrderItemMapper</li>
 *   <li>totalAmount is computed by entity method (recalculateTotal)</li>
 *   <li>UpdateDto → Entity: partial update (status, dates, notes)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {PurchaseOrderItemMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseOrderMapper {

    /**
     * Maps a CreateDto to a PurchaseOrder entity.
     * <p>supplierId is ignored — service layer will resolve and set the Supplier entity.</p>
     * <p>items are mapped via PurchaseOrderItemMapper.</p>
     */
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderCreateDto dto);

    /**
     * Maps a PurchaseOrder entity to a ResponseDto.
     * <p>Includes nested supplier and items.</p>
     */
    @Mapping(target = "supplier", source = "supplier")
    PurchaseOrderResponseDto toResponseDto(PurchaseOrder entity);

    /**
     * Maps a list of PurchaseOrder entities to ResponseDto list.
     */
    List<PurchaseOrderResponseDto> toResponseDtoList(List<PurchaseOrder> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing PurchaseOrder entity.
     * <p>Only status, dates, and notes can be updated.</p>
     * <p>supplier, orderDate, items are immutable after creation.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PurchaseOrderUpdateDto dto, @MappingTarget PurchaseOrder entity);
}
