package hospital.coreservice.mapper;

import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.dto.shift.ShiftUpdateDto;
import hospital.coreservice.model.Shift;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
/**
 * MapStruct mapper for Shift entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface ShiftMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Shift toEntity(ShiftCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Shift shift, ShiftUpdateDto updateDto);

    ShiftResponseDto toResponseDto(Shift shift);
}