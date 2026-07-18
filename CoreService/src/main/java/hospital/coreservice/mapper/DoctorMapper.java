package hospital.coreservice.mapper;

import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.doctor.DoctorUpdateDto;
import hospital.coreservice.model.Doctor;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DepartmentSlimMapper.class, DoctorScheduleMapper.class}
)
/**
 * MapStruct mapper for Doctor entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface DoctorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorSchedules", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Doctor toEntity(DoctorCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorSchedules", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateEntity(@MappingTarget Doctor doctor, DoctorUpdateDto updateDto);

    @Mapping(target = "fullName", expression = "java(doctor.getFirstName() + \" \" + doctor.getLastName())")
    DoctorResponseDto toResponseDto(Doctor doctor);

}