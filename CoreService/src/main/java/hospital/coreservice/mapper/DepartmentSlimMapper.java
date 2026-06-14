package hospital.coreservice.mapper;

import hospital.coreservice.dto.department.DepartmentSlimResponseDto;
import hospital.coreservice.model.Department;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentSlimMapper {

    DepartmentSlimResponseDto toSlimDto(Department department);

    List<DepartmentSlimResponseDto> toSlimDtoList(List<Department> departments);
}