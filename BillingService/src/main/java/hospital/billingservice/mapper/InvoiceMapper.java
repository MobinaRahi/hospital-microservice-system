package hospital.billingservice.mapper;

import hospital.billingservice.dto.invoice.InvoiceCreateDto;
import hospital.billingservice.dto.invoice.InvoiceResponseDto;
import hospital.billingservice.dto.invoice.InvoiceUpdateDto;
import hospital.billingservice.model.Invoice;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Invoice entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {InvoiceItemMapper.class, PaymentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {

    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "patientShare", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Invoice toEntity(InvoiceCreateDto dto);

    @Mapping(target = "isOverdue", expression = "java(entity.isOverdue())")
    @Mapping(target = "payments", source = "payments")
    InvoiceResponseDto toResponseDto(Invoice entity);

    List<InvoiceResponseDto> toResponseDtoList(List<Invoice> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "issueDate", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "patientShare", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "payments", ignore = true)
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
    void updateEntity(InvoiceUpdateDto dto, @MappingTarget Invoice entity);
}
