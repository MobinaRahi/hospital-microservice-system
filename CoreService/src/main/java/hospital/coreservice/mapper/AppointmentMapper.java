package hospital.coreservice.mapper;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.model.Appointment;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PatientMapper.class, DoctorMapper.class, DepartmentMapper.class}
)
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


    AppointmentResponseDto toResponseDto(Appointment appointment);
}