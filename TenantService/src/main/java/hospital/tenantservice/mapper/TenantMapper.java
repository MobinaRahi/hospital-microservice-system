package hospital.tenantservice.mapper;

import hospital.tenantservice.dto.tenant.*;
import hospital.tenantservice.model.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Tenant entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps basic info, sets plan-specific limits</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 *   <li>Entity → ResponseDto: Maps all fields + computed fields</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantMapper {

    /**
     * Converts TenantCreateDto to Tenant entity.
     * Plan-specific limits are set by the service layer.
     *
     * @param dto the create DTO
     * @return Tenant entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maxUsers", ignore = true)
    @Mapping(target = "maxPatients", ignore = true)
    @Mapping(target = "maxAppointmentsPerMonth", ignore = true)
    @Mapping(target = "storageLimitMB", ignore = true)
    @Mapping(target = "supportLevel", ignore = true)
    @Mapping(target = "currentUsers", ignore = true)
    @Mapping(target = "currentPatients", ignore = true)
    @Mapping(target = "currentMonthAppointments", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Tenant toEntity(TenantCreateDto dto);

    /**
     * Converts Tenant entity to TenantResponseDto.
     *
     * @param entity the Tenant entity
     * @return TenantResponseDto
     */
    @Mapping(target = "isExpired", expression = "java(entity.isExpired())")
    @Mapping(target = "isOperational", expression = "java(entity.isOperational())")
    TenantResponseDto toResponseDto(Tenant entity);

    /**
     * Converts list of Tenant entities to list of DTOs.
     *
     * @param entities list of Tenant entities
     * @return list of TenantResponseDto
     */
    List<TenantResponseDto> toResponseDtoList(List<Tenant> entities);

    /**
     * Updates existing Tenant entity from UpdateDto.
     * Null values in DTO are ignored.
     * Critical fields (subdomain, plan) are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subdomain", ignore = true)
    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "maxUsers", ignore = true)
    @Mapping(target = "maxPatients", ignore = true)
    @Mapping(target = "maxAppointmentsPerMonth", ignore = true)
    @Mapping(target = "storageLimitMB", ignore = true)
    @Mapping(target = "supportLevel", ignore = true)
    @Mapping(target = "currentUsers", ignore = true)
    @Mapping(target = "currentPatients", ignore = true)
    @Mapping(target = "currentMonthAppointments", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(TenantUpdateDto dto, @MappingTarget Tenant entity);

    /**
     * Converts Tenant entity to TenantUsageResponseDto.
     * Usage percentages are calculated by the service layer.
     *
     * @param entity the Tenant entity
     * @return TenantUsageResponseDto
     */
    @Mapping(target = "tenantId", source = "id")
    @Mapping(target = "tenantName", source = "name")
    @Mapping(target = "userUsagePercent", ignore = true)
    @Mapping(target = "patientUsagePercent", ignore = true)
    @Mapping(target = "appointmentUsagePercent", ignore = true)
    @Mapping(target = "storageUsagePercent", ignore = true)
    @Mapping(target = "storageUsedMB", ignore = true)
    @Mapping(target = "isExpired", expression = "java(entity.isExpired())")
    @Mapping(target = "checkedAt", expression = "java(java.time.LocalDateTime.now())")
    TenantUsageResponseDto toUsageResponseDto(Tenant entity);

    /**
     * Converts list of Tenant entities to list of UsageResponseDto.
     *
     * @param entities list of Tenant entities
     * @return list of TenantUsageResponseDto
     */
    List<TenantUsageResponseDto> toUsageResponseDtoList(List<Tenant> entities);
}
