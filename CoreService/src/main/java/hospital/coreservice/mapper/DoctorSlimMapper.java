package hospital.coreservice.mapper;

import hospital.coreservice.dto.doctor.DoctorSlimResponseDto;
import hospital.coreservice.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
/**
 * MapStruct mapper for Doctor entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface DoctorSlimMapper {

    @Mapping(target = "fullName", expression = "java(doctor.getFirstName() + \" \" + doctor.getLastName())")
    DoctorSlimResponseDto toSlimDto(Doctor doctor);

    List<DoctorSlimResponseDto> toSlimDtoList(List<Doctor> doctors);
}