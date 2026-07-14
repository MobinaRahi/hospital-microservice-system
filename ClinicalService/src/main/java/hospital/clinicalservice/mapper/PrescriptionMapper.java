package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.prescription.PrescriptionCreateDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionUpdateDto;
import hospital.clinicalservice.model.Prescription;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PrescriptionItemMapper.class}
)
public interface PrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "prescribedDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "deleted", ignore = true)
    Prescription toEntity(PrescriptionCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounterId", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "prescribedDate", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntity(@MappingTarget Prescription prescription, PrescriptionUpdateDto updateDto);

    @Mapping(target = "encounterId", expression = "java(prescription.getEncounter() != null ? prescription.getEncounter().getId() : null)")
    PrescriptionResponseDto toResponseDto(Prescription prescription);
}
