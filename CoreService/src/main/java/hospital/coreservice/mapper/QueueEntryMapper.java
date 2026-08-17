package hospital.coreservice.mapper;

import hospital.coreservice.dto.queue.QueueEntryResponseDto;
import hospital.coreservice.model.QueueEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for QueueEntry entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QueueEntryMapper {

    @Mapping(target = "isForToday", expression = "java(entity.isForToday())")
    @Mapping(target = "isEmergency", expression = "java(entity.isEmergency())")
    QueueEntryResponseDto toResponseDto(QueueEntry entity);

    List<QueueEntryResponseDto> toResponseDtoList(List<QueueEntry> entities);
}
