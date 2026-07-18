package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemCreateDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemResponseDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemUpdateDto;
import hospital.clinicalservice.model.PrescriptionItem;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PrescriptionItemMapper {

    /**
     * Converts CreateDto to Entity.
     * prescriptionId is ignored because entity has prescription (object).
     * Service layer handles setting the prescription from prescriptionId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    PrescriptionItem toEntity(PrescriptionItemCreateDto createDto);

    /**
     * Updates Entity from UpdateDto.
     * prescriptionId and drugId are immutable after creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    void updateEntity(@MappingTarget PrescriptionItem item, PrescriptionItemUpdateDto updateDto);

    /**
     * Converts Entity to ResponseDto.
     * prescriptionId is extracted from prescription object.
     */
    @Mapping(target = "prescriptionId", expression = "java(item.getPrescription() != null ? item.getPrescription().getId() : null)")
    PrescriptionItemResponseDto toResponseDto(PrescriptionItem item);
}
