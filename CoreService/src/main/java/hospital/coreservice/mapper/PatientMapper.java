package hospital.coreservice.mapper;

import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.model.Patient;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoomMapper.class}
)
/**
 * MapStruct mapper for Patient entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "currentRoom", ignore = true)
    Patient toEntity(PatientCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "currentRoom", ignore = true)
    void updateEntity(@MappingTarget Patient patient, PatientUpdateDto updateDto);

    @Mapping(target = "fullName", expression = "java(patient.getFirstName() + \" \" + patient.getLastName())")
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    PatientResponseDto toResponseDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nationalId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "insuranceId", ignore = true)
    @Mapping(target = "currentRoom", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    void updatePatientFromRegistration(@MappingTarget Patient patient, CompleteRegistrationRequest request);
}