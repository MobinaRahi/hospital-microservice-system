package hospital.billingservice.mapper;

import hospital.billingservice.dto.servicecatalog.ServiceCatalogCreateDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogResponseDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogUpdateDto;
import hospital.billingservice.model.ServiceCatalog;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for ServiceCatalog entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceCatalogMapper {

    @Mapping(target = "tenantId", ignore = true)
    ServiceCatalog toEntity(ServiceCatalogCreateDto dto);

    ServiceCatalogResponseDto toResponseDto(ServiceCatalog entity);

    List<ServiceCatalogResponseDto> toResponseDtoList(List<ServiceCatalog> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(ServiceCatalogUpdateDto dto, @MappingTarget ServiceCatalog entity);
}
