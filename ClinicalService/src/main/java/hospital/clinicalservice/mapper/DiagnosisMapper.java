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

    /**
     * Converts CreateDto to Entity.
     * encounterId is ignored because entity has encounter (object).
     * Service layer handles setting the encounter from encounterId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Diagnosis toEntity(DiagnosisCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * encounterId and icd10Code are immutable after creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    void updateEntity(@MappingTarget Diagnosis diagnosis, DiagnosisUpdateDto updateDto);

    /**
     * Converts Entity to ResponseDto.
     * encounterId is extracted from encounter object.
     */
    @Mapping(target = "encounterId", expression = "java(diagnosis.getEncounter() != null ? diagnosis.getEncounter().getId() : null)")
    DiagnosisResponseDto toResponseDto(Diagnosis diagnosis);
}
