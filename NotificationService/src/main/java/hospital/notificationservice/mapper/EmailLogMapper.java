package hospital.notificationservice.mapper;

import hospital.notificationservice.dto.emaillog.EmailLogCreateDto;
import hospital.notificationservice.dto.emaillog.EmailLogResponseDto;
import hospital.notificationservice.model.EmailLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for EmailLog entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps to, subject, body, templateId;
 *       status, sentAt, errorMessage are set by business logic</li>
 *   <li>Entity → ResponseDto: Maps all fields including status, timestamps, and errors</li>
 * </ul>
 *
 * <p><strong>Note:</strong></p>
 * <p>EmailLog does not have an UpdateDto because email records are immutable
 * once created — only status transitions are allowed via business methods.</p>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailLogMapper {

    /**
     * Converts EmailLogCreateDto to EmailLog entity.
     * Status defaults to PENDING (set by entity).
     * sentAt and errorMessage are set by business methods.
     *
     * @param dto the create DTO
     * @return EmailLog entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    EmailLog toEntity(EmailLogCreateDto dto);

    /**
     * Converts EmailLog entity to EmailLogResponseDto.
     *
     * @param entity the EmailLog entity
     * @return EmailLogResponseDto
     */
    EmailLogResponseDto toResponseDto(EmailLog entity);

    /**
     * Converts list of EmailLog entities to list of DTOs.
     *
     * @param entities list of EmailLog entities
     * @return list of EmailLogResponseDto
     */
    List<EmailLogResponseDto> toResponseDtoList(List<EmailLog> entities);
}
