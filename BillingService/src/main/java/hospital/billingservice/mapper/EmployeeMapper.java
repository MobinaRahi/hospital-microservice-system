package hospital.billingservice.mapper;

import hospital.billingservice.dto.employee.EmployeeCreateDto;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.dto.employee.EmployeeUpdateDto;
import hospital.billingservice.model.Employee;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Employee entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "payrolls", ignore = true)
    Employee toEntity(EmployeeCreateDto dto);

    @Mapping(target = "yearsOfService", expression = "java(entity.getYearsOfService())")
    EmployeeResponseDto toResponseDto(Employee entity);

    List<EmployeeResponseDto> toResponseDtoList(List<Employee> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "payrolls", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(EmployeeUpdateDto dto, @MappingTarget Employee entity);
}
