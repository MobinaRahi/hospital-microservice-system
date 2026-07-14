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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "triagedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", ignore = true)
    Triage toEntity(TriageCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "triagedBy", ignore = true)
    @Mapping(target = "triagedAt", ignore = true)
    void updateEntity(@MappingTarget Triage triage, TriageUpdateDto updateDto);

    @Mapping(target = "encounterId", expression = "java(triage.getEncounter() != null ? triage.getEncounter().getId() : null)")
    TriageResponseDto toResponseDto(Triage triage);
}
