package hospital.tenantservice.mapper;

import hospital.tenantservice.dto.subscriptionhistory.SubscriptionHistoryResponseDto;
import hospital.tenantservice.model.SubscriptionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for SubscriptionHistory entity and DTOs.
 *
 * <p>SubscriptionHistory is a read-only audit entity, so only toResponseDto is provided.</p>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionHistoryMapper {

    /**
     * Converts SubscriptionHistory entity to SubscriptionHistoryResponseDto.
     *
     * @param entity the SubscriptionHistory entity
     * @return SubscriptionHistoryResponseDto
     */
    @Mapping(target = "isEffective", expression = "java(entity.isEffective())")
    @Mapping(target = "isScheduled", expression = "java(entity.isScheduled())")
    SubscriptionHistoryResponseDto toResponseDto(SubscriptionHistory entity);

    /**
     * Converts list of SubscriptionHistory entities to list of DTOs.
     *
     * @param entities list of SubscriptionHistory entities
     * @return list of SubscriptionHistoryResponseDto
     */
    List<SubscriptionHistoryResponseDto> toResponseDtoList(List<SubscriptionHistory> entities);
}
