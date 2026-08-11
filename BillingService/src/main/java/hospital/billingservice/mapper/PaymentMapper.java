package hospital.billingservice.mapper;

import hospital.billingservice.dto.payment.PaymentCreateDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.dto.payment.PaymentUpdateDto;
import hospital.billingservice.model.Payment;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Payment entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Payment toEntity(PaymentCreateDto dto);

    PaymentResponseDto toResponseDto(Payment entity);

    List<PaymentResponseDto> toResponseDtoList(List<Payment> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PaymentUpdateDto dto, @MappingTarget Payment entity);
}
