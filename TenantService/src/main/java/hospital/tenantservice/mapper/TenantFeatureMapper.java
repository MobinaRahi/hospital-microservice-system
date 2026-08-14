package hospital.tenantservice.mapper;

import hospital.tenantservice.dto.tenantfeature.TenantFeatureCreateDto;
import hospital.tenantservice.dto.tenantfeature.TenantFeatureResponseDto;
import hospital.tenantservice.model.TenantFeature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for TenantFeature entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps feature details</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantFeatureMapper {

    /**
     * Converts TenantFeatureCreateDto to TenantFeature entity.
     *
     * @param dto the create DTO
     * @return TenantFeature entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    TenantFeature toEntity(TenantFeatureCreateDto dto);

    /**
     * Converts TenantFeature entity to TenantFeatureResponseDto.
     *
     * @param entity the TenantFeature entity
     * @return TenantFeatureResponseDto
     */
    TenantFeatureResponseDto toResponseDto(TenantFeature entity);

    /**
     * Converts list of TenantFeature entities to list of DTOs.
     *
     * @param entities list of TenantFeature entities
     * @return list of TenantFeatureResponseDto
     */
    List<TenantFeatureResponseDto> toResponseDtoList(List<TenantFeature> entities);
}
