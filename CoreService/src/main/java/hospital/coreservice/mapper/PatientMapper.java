package hospital.coreservice.mapper;

import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.model.Patient;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoomMapper.class}
)
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "currentRoom", ignore = true)
    @Mapping(target = "user", ignore = true)
    Patient toEntity(PatientCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "currentRoom", ignore = true)
    void updateEntity(@MappingTarget Patient patient, PatientUpdateDto updateDto);

    @Mapping(target = "fullName", expression = "java(patient.getFirstName() + \" \" + patient.getLastName())")
    PatientResponseDto toResponseDto(Patient patient);
}