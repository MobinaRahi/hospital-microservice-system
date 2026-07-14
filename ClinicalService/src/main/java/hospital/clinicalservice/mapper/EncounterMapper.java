package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.model.Encounter;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DiagnosisMapper.class, PrescriptionMapper.class, ObservationMapper.class, NursingNoteMapper.class}
)
public interface EncounterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnoses", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "nursingNotes", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Encounter toEntity(EncounterCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "appointmentId", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "encounterDate", ignore = true)
    @Mapping(target = "diagnoses", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "nursingNotes", ignore = true)
    void updateEntity(@MappingTarget Encounter encounter, EncounterUpdateDto updateDto);

    EncounterResponseDto toResponseDto(Encounter encounter);
}
