package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.observation.ObservationCreateDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.observation.ObservationUpdateDto;
import hospital.clinicalservice.model.Observation;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
/**
 * MapStruct mapper for Observation entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface ObservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "observedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", ignore = true)
    Observation toEntity(ObservationCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "loincCode", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "referenceRangeLow", ignore = true)
    @Mapping(target = "referenceRangeHigh", ignore = true)
    @Mapping(target = "observedAt", ignore = true)
    void updateEntity(@MappingTarget Observation observation, ObservationUpdateDto updateDto);

    @Mapping(target = "encounterId", expression = "java(observation.getEncounter() != null ? observation.getEncounter().getId() : null)")
    ObservationResponseDto toResponseDto(Observation observation);
}
