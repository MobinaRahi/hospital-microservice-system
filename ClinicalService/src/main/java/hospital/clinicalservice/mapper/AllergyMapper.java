package hospital.clinicalservice.mapper;

import hospital.clinicalservice.dto.allergy.AllergyCreateDto;
import hospital.clinicalservice.dto.allergy.AllergyResponseDto;
import hospital.clinicalservice.dto.allergy.AllergyUpdateDto;
import hospital.clinicalservice.model.Allergy;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AllergyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Allergy toEntity(AllergyCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    void updateEntity(@MappingTarget Allergy allergy, AllergyUpdateDto updateDto);

    AllergyResponseDto toResponseDto(Allergy allergy);
}
