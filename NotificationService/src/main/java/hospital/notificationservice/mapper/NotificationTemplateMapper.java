package hospital.notificationservice.mapper;

import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateCreateDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateResponseDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateUpdateDto;
import hospital.notificationservice.model.NotificationTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for NotificationTemplate entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps name, type, subject, content, variables;
 *       isActive defaults to true (set by entity)</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationTemplateMapper {

    /**
     * Converts NotificationTemplateCreateDto to NotificationTemplate entity.
     * isActive defaults to true (set by entity).
     * updatedAt is set by business methods.
     *
     * @param dto the create DTO
     * @return NotificationTemplate entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    NotificationTemplate toEntity(NotificationTemplateCreateDto dto);

    /**
     * Converts NotificationTemplate entity to NotificationTemplateResponseDto.
     *
     * @param entity the NotificationTemplate entity
     * @return NotificationTemplateResponseDto
     */
    NotificationTemplateResponseDto toResponseDto(NotificationTemplate entity);

    /**
     * Converts list of NotificationTemplate entities to list of DTOs.
     *
     * @param entities list of NotificationTemplate entities
     * @return list of NotificationTemplateResponseDto
     */
    List<NotificationTemplateResponseDto> toResponseDtoList(List<NotificationTemplate> entities);

    /**
     * Updates existing NotificationTemplate entity from UpdateDto.
     * Null values in DTO are ignored.
     * Audit fields are not updated directly (managed by JPA auditing).
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
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
    void updateEntity(NotificationTemplateUpdateDto dto, @MappingTarget NotificationTemplate entity);
}
