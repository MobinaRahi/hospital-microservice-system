package hospital.billingservice.mapper;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemUpdateDto;
import hospital.billingservice.model.InvoiceItem;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for InvoiceItem entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceItemMapper {

    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    InvoiceItem toEntity(InvoiceItemCreateDto dto);

    InvoiceItemResponseDto toResponseDto(InvoiceItem entity);

    List<InvoiceItemResponseDto> toResponseDtoList(List<InvoiceItem> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "serviceCode", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(InvoiceItemUpdateDto dto, @MappingTarget InvoiceItem entity);
}
