package com.hospital.superadmin.mapper;

import com.hospital.superadmin.dto.plan.PlanCreateDto;
import com.hospital.superadmin.dto.plan.PlanResponseDto;
import com.hospital.superadmin.dto.plan.PlanUpdateDto;
import com.hospital.superadmin.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Plan entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Plan toEntity(PlanCreateDto dto);

    @Mapping(target = "isFree", expression = "java(entity.isFree())")
    @Mapping(target = "isEnterprise", expression = "java(entity.isEnterprise())")
    PlanResponseDto toResponseDto(Plan entity);

    List<PlanResponseDto> toResponseDtoList(List<Plan> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(PlanUpdateDto dto, @MappingTarget Plan entity);
}
