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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "recordedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", ignore = true)
    NursingNote toEntity(NursingNoteCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "nurseId", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    void updateEntity(@MappingTarget NursingNote note, NursingNoteUpdateDto updateDto);

    @Mapping(target = "encounterId", expression = "java(note.getEncounter() != null ? note.getEncounter().getId() : null)")
    NursingNoteResponseDto toResponseDto(NursingNote note);
}
