package hospital.notificationservice.mapper;

import hospital.notificationservice.dto.smsgateway.SMSGatewayCreateDto;
import hospital.notificationservice.dto.smsgateway.SMSGatewayResponseDto;
import hospital.notificationservice.model.SMSGateway;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for SMSGateway entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps to, message, templateId, provider, cost;
 *       status, providerMessageId, sentAt, deliveredAt, errorMessage are set by business logic</li>
 *   <li>Entity → ResponseDto: Maps all fields including status, timestamps, and errors</li>
 * </ul>
 *
 * <p><strong>Note:</strong></p>
 * <p>SMSGateway does not have an UpdateDto because SMS messages are immutable
 * once created — only status transitions are allowed via business methods.</p>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SMSGatewayMapper {

    /**
     * Converts SMSGatewayCreateDto to SMSGateway entity.
     * Status defaults to PENDING (set by entity).
     * providerMessageId, sentAt, deliveredAt, errorMessage are set by business methods.
     *
     * @param dto the create DTO
     * @return SMSGateway entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "providerMessageId", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SMSGateway toEntity(SMSGatewayCreateDto dto);

    /**
     * Converts SMSGateway entity to SMSGatewayResponseDto.
     *
     * @param entity the SMSGateway entity
     * @return SMSGatewayResponseDto
     */
    SMSGatewayResponseDto toResponseDto(SMSGateway entity);

    /**
     * Converts list of SMSGateway entities to list of DTOs.
     *
     * @param entities list of SMSGateway entities
     * @return list of SMSGatewayResponseDto
     */
    List<SMSGatewayResponseDto> toResponseDtoList(List<SMSGateway> entities);
}
