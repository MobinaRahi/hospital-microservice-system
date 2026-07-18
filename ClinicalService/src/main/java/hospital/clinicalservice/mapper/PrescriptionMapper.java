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

    /**
     * Converts CreateDto to Entity.
     * encounterId is ignored because entity has encounter (object).
     * Service layer handles setting the encounter from encounterId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "prescribedDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "deleted", ignore = true)
    Prescription toEntity(PrescriptionCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * encounterId, patientId, doctorId are immutable after creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntity(@MappingTarget Prescription prescription, PrescriptionUpdateDto updateDto);

    /**
     * Converts Entity to ResponseDto.
     * encounterId is extracted from encounter object.
     */
    @Mapping(target = "encounterId", expression = "java(prescription.getEncounter() != null ? prescription.getEncounter().getId() : null)")
    PrescriptionResponseDto toResponseDto(Prescription prescription);
}
