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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    PrescriptionItem toEntity(PrescriptionItemCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptionId", ignore = true)
    @Mapping(target = "drugId", ignore = true)
    @Mapping(target = "drugName", ignore = true)
    void updateEntity(@MappingTarget PrescriptionItem item, PrescriptionItemUpdateDto updateDto);

    @Mapping(target = "prescriptionId", expression = "java(item.getPrescription() != null ? item.getPrescription().getId() : null)")
    PrescriptionItemResponseDto toResponseDto(PrescriptionItem item);
}
