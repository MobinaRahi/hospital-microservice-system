package hospital.billingservice.mapper;

import hospital.billingservice.dto.insurancemanagement.InsuranceManagementCreateDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementUpdateDto;
import hospital.billingservice.model.InsuranceManagement;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for InsuranceManagement entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InsuranceManagementMapper {

    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "patientInsurances", ignore = true)
    InsuranceManagement toEntity(InsuranceManagementCreateDto dto);

    InsuranceManagementResponseDto toResponseDto(InsuranceManagement entity);

    List<InsuranceManagementResponseDto> toResponseDtoList(List<InsuranceManagement> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "patientInsurances", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(InsuranceManagementUpdateDto dto, @MappingTarget InsuranceManagement entity);
}
