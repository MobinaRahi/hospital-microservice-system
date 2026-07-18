package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.nursingnote.NursingNoteCreateDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteUpdateDto;
import hospital.clinicalservice.model.NursingNote;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NursingNoteMapper {

    /**
     * Converts CreateDto to Entity.
     * encounterId is ignored because entity has encounter (object).
     * Service layer handles setting the encounter from encounterId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "recordedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", ignore = true)
    NursingNote toEntity(NursingNoteCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * encounterId, patientId, nurseId are immutable after creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "nurseId", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    void updateEntity(@MappingTarget NursingNote note, NursingNoteUpdateDto updateDto);

    /**
     * Converts Entity to ResponseDto.
     * encounterId is extracted from encounter object.
     */
    @Mapping(target = "encounterId", expression = "java(note.getEncounter() != null ? note.getEncounter().getId() : null)")
    NursingNoteResponseDto toResponseDto(NursingNote note);
}
