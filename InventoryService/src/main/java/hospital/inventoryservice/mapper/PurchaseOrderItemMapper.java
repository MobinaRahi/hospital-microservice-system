package hospital.inventoryservice.mapper;

import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemResponseDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemUpdateDto;
import hospital.inventoryservice.model.PurchaseOrderItem;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for PurchaseOrderItem entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: drugId and purchaseOrderId are ignored — service resolves entities</li>
 *   <li>Entity → ResponseDto: drug is mapped as nested DrugResponseDto</li>
 *   <li>subtotal is computed via entity method (unitPrice × receivedQuantity)</li>
 *   <li>UpdateDto → Entity: partial update (quantity, price, receivedQuantity)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {DrugMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseOrderItemMapper {

    /**
     * Maps a CreateDto to a PurchaseOrderItem entity.
     * <p>drugId and purchaseOrderId are ignored — service layer resolves entities.</p>
     */
    @Mapping(target = "drug", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "receivedQuantity", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "tenantId", ignore = true)
    PurchaseOrderItem toEntity(PurchaseOrderItemCreateDto dto);

    /**
     * Maps a PurchaseOrderItem entity to a ResponseDto.
     * <p>Includes nested drug information and computed subtotal.</p>
     */
    @Mapping(target = "drug", source = "drug")
    @Mapping(target = "subtotal", expression = "java(entity.getSubtotal())")
    PurchaseOrderItemResponseDto toResponseDto(PurchaseOrderItem entity);

    /**
     * Maps a list of PurchaseOrderItem entities to ResponseDto list.
     */
    List<PurchaseOrderItemResponseDto> toResponseDtoList(List<PurchaseOrderItem> entities);

    /**
     * Applies non-null fields from UpdateDto to an existing PurchaseOrderItem entity.
     * <p>drug and purchaseOrder are immutable after creation.</p>
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drug", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PurchaseOrderItemUpdateDto dto, @MappingTarget PurchaseOrderItem entity);
}
