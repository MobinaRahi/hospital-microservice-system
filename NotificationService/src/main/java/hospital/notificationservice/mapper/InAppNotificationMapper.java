package hospital.notificationservice.mapper;

import hospital.notificationservice.dto.inappnotification.InAppNotificationCreateDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationResponseDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationUpdateDto;
import hospital.notificationservice.model.InAppNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for InAppNotification entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps userId, title, message, type, relatedId;
 *       isRead, readAt are set by business logic</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 *   <li>Entity → ResponseDto: Maps all fields</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InAppNotificationMapper {

    /**
     * Converts InAppNotificationCreateDto to InAppNotification entity.
     * isRead defaults to false (set by entity).
     * readAt is set by markAsRead() business method.
     *
     * @param dto the create DTO
     * @return InAppNotification entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    InAppNotification toEntity(InAppNotificationCreateDto dto);

    /**
     * Converts InAppNotification entity to InAppNotificationResponseDto.
     *
     * @param entity the InAppNotification entity
     * @return InAppNotificationResponseDto
     */
    InAppNotificationResponseDto toResponseDto(InAppNotification entity);

    /**
     * Converts list of InAppNotification entities to list of DTOs.
     *
     * @param entities list of InAppNotification entities
     * @return list of InAppNotificationResponseDto
     */
    List<InAppNotificationResponseDto> toResponseDtoList(List<InAppNotification> entities);

    /**
     * Updates existing InAppNotification entity from UpdateDto.
     * Null values in DTO are ignored.
     * Identity fields (userId, type, relatedId) and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "relatedId", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(InAppNotificationUpdateDto dto, @MappingTarget InAppNotification entity);
}
