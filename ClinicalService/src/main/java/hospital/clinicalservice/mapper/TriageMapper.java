package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.triage.TriageCreateDto;
import hospital.clinicalservice.dto.triage.TriageResponseDto;
import hospital.clinicalservice.dto.triage.TriageUpdateDto;
import hospital.clinicalservice.model.Triage;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TriageMapper {

    /**
     * Converts CreateDto to Entity.
     * encounterId is ignored because entity has encounter (object).
     * Service layer handles setting the encounter from encounterId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "triagedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", ignore = true)
    Triage toEntity(TriageCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * encounterId, patientId, triagedBy are immutable after creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "triagedBy", ignore = true)
    @Mapping(target = "triagedAt", ignore = true)
    void updateEntity(@MappingTarget Triage triage, TriageUpdateDto updateDto);

    /**
     * Converts Entity to ResponseDto.
     * encounterId is extracted from encounter object.
     */
    @Mapping(target = "encounterId", expression = "java(triage.getEncounter() != null ? triage.getEncounter().getId() : null)")
    TriageResponseDto toResponseDto(Triage triage);
}
