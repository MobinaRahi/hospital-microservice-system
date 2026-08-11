package hospital.billingservice.mapper;

import hospital.billingservice.dto.payroll.PayrollCreateDto;
import hospital.billingservice.dto.payroll.PayrollResponseDto;
import hospital.billingservice.dto.payroll.PayrollUpdateDto;
import hospital.billingservice.model.Payroll;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Payroll entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {EmployeeMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayrollMapper {

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "netSalary", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Payroll toEntity(PayrollCreateDto dto);

    @Mapping(target = "employee", source = "employee")
    PayrollResponseDto toResponseDto(Payroll entity);

    List<PayrollResponseDto> toResponseDtoList(List<Payroll> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "baseSalary", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PayrollUpdateDto dto, @MappingTarget Payroll entity);
}
