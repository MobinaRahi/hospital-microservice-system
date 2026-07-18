package hospital.coreservice.mapper;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.model.Appointment;
import hospital.coreservice.model.Department;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PatientMapper.class, DoctorMapper.class, DepartmentMapper.class}
)
/**
 * MapStruct mapper for Appointment entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "canceledBy", ignore = true)
    @Mapping(target = "canceledReason", ignore = true)
    @Mapping(target = "canceledAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Appointment toEntity(AppointmentCreateDto createDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "canceledAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(@MappingTarget Appointment appointment, AppointmentUpdateDto updateDto);


    @Mapping(target = "department", ignore = true)
    AppointmentResponseDto toResponseDto(Appointment appointment);

    @AfterMapping
    default void mapDepartmentSummary(Appointment appointment, @MappingTarget AppointmentResponseDto responseDto) {
        Department department = appointment.getDepartment();
        if (department == null) {
            return;
        }
        DepartmentResponseDto departmentDto = new DepartmentResponseDto();
        departmentDto.setId(department.getId());
        departmentDto.setDepartmentName(department.getDepartmentName());
        departmentDto.setDepartmentCode(department.getDepartmentCode());
        departmentDto.setDescription(department.getDescription());
        departmentDto.setLocation(department.getLocation());
        departmentDto.setPhoneNumber(department.getPhoneNumber());
        departmentDto.setIsActive(department.isActive());
        departmentDto.setCreatedAt(department.getCreatedAt());
        departmentDto.setUpdatedAt(department.getUpdatedAt());
        responseDto.setDepartment(departmentDto);
    }
}