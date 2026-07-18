package hospital.coreservice.mapper;

import hospital.coreservice.dto.nurse.NurseSlimResponseDto;
import hospital.coreservice.model.Nurse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
/**
 * MapStruct mapper for Nurse entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface NurseSlimMapper {

    @Mapping(target = "fullName", expression = "java(nurse.getFirstName() + \" \" + nurse.getLastName())")
    NurseSlimResponseDto toSlimDto(Nurse nurse);

    List<NurseSlimResponseDto> toSlimDtoList(List<Nurse> nurses);
}