package hospital.coreservice.mapper;

import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.department.DepartmentUpdateDto;
import hospital.coreservice.model.Department;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DoctorSlimMapper.class, NurseSlimMapper.class, RoomMapper.class}
)
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "headDoctor", ignore = true)
    @Mapping(target = "headNurse", ignore = true)
    @Mapping(target = "doctorList", ignore = true)
    @Mapping(target = "nurseList", ignore = true)
    @Mapping(target = "roomList", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Department toEntity(DepartmentCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "headDoctor", ignore = true)
    @Mapping(target = "headNurse", ignore = true)
    @Mapping(target = "doctorList", ignore = true)
    @Mapping(target = "nurseList", ignore = true)
    @Mapping(target = "roomList", ignore = true)
    void updateEntity(@MappingTarget Department department, DepartmentUpdateDto updateDto);

    DepartmentResponseDto toResponseDto(Department department);
}