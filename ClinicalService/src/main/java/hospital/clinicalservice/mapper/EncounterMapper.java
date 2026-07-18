package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.model.Encounter;
import org.mapstruct.*;

/**
 * MapStruct mapper for Encounter entity ↔ DTO conversion.
 *
 * <p><strong>Mapping rules:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Ignores id, status, relations (set by service)</li>
 *   <li>UpdateDto → Entity: Only updates non-null fields (NullValuePropertyMappingStrategy.IGNORE)</li>
 *   <li>Entity → ResponseDto: Includes all fields + nested DTOs</li>
 * </ul>
 *
 * @author Mobina
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DiagnosisMapper.class, PrescriptionMapper.class, ObservationMapper.class, NursingNoteMapper.class}
)
public interface EncounterMapper {

    /**
     * Converts CreateDto to Entity.
     * Ignores: id (auto-generated), status (set by service), relations (empty lists).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnoses", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "nursingNotes", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Encounter toEntity(EncounterCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * Only updates fields that are not null in UpdateDto.
     * Ignores: id, patientId, doctorId, appointmentId, departmentId, encounterDate, relations.
     */
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

    /**
     * Converts Entity to ResponseDto.
     * Includes all fields + nested DTOs (diagnoses, prescriptions, etc.).
     */
    EncounterResponseDto toResponseDto(Encounter encounter);
}
