package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.diagnosis.DiagnosisCreateDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisUpdateDto;
import hospital.clinicalservice.model.Diagnosis;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DiagnosisMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Diagnosis toEntity(DiagnosisCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "icd10Code", ignore = true)
    void updateEntity(@MappingTarget Diagnosis diagnosis, DiagnosisUpdateDto updateDto);

    @Mapping(target = "encounterId", expression = "java(diagnosis.getEncounter() != null ? diagnosis.getEncounter().getId() : null)")
    DiagnosisResponseDto toResponseDto(Diagnosis diagnosis);
}
