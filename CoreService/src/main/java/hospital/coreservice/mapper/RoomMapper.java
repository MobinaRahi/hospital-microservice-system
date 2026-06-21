package hospital.coreservice.mapper;

import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.dto.room.RoomUpdateDto;
import hospital.coreservice.model.Room;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {DepartmentMapper.class, PatientMapper.class}
)
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "currentPatientList", ignore = true)
    @Mapping(target = "isOccupied", ignore = true)
    Room toEntity(RoomCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "currentPatientList", ignore = true)
    void updateEntity(@MappingTarget Room room, RoomUpdateDto updateDto);

    @Mapping(target = "currentOccupancy", ignore = true)
    @Mapping(target = "availableCapacity", ignore = true)
    RoomResponseDto toResponseDto(Room room);
}
