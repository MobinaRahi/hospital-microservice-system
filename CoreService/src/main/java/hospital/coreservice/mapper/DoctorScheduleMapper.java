package hospital.coreservice.mapper;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.DoctorSchedule;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DoctorMapper.class}
)
public interface DoctorScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    DoctorSchedule toEntity(DoctorScheduleCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    void updateEntity(@MappingTarget DoctorSchedule doctorSchedule, DoctorScheduleUpdateDto updateDto);

    DoctorScheduleResponseDto toResponseDto(DoctorSchedule doctorSchedule);
}