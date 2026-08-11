package hospital.billingservice.mapper;

import hospital.billingservice.dto.patientinsurance.PatientInsuranceCreateDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceResponseDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceUpdateDto;
import hospital.billingservice.model.PatientInsurance;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for PatientInsurance entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {InsuranceManagementMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientInsuranceMapper {

    @Mapping(target = "insurance", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    PatientInsurance toEntity(PatientInsuranceCreateDto dto);

    @Mapping(target = "insurance", source = "insurance")
    PatientInsuranceResponseDto toResponseDto(PatientInsurance entity);

    List<PatientInsuranceResponseDto> toResponseDtoList(List<PatientInsurance> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "insurance", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PatientInsuranceUpdateDto dto, @MappingTarget PatientInsurance entity);
}
