package hospital.coreservice.mapper;

import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.nurse.NurseUpdateDto;
import hospital.coreservice.model.Nurse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DepartmentSlimMapper.class, ShiftMapper.class}
)
public interface NurseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentList", ignore = true)
    @Mapping(target = "shiftPreferenceList", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Nurse toEntity(NurseCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentList", ignore = true)
    @Mapping(target = "shiftPreferenceList", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Nurse nurse, NurseUpdateDto updateDto);

    @Mapping(target = "fullName", expression = "java(nurse.getFirstName() + \" \" + nurse.getLastName())")
    NurseResponseDto toResponseDto(Nurse nurse);
}