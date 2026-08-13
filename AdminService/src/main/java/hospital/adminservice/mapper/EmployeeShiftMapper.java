package hospital.adminservice.mapper;

import hospital.adminservice.dto.employeeshift.EmployeeShiftCreateDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftResponseDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftUpdateDto;
import hospital.adminservice.model.EmployeeShift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for EmployeeShift entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps employeeId, shiftId, date, flags</li>
 *   <li>Entity → ResponseDto: Maps all fields + nested Shift</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored)</li>
 * </ul>
 *
 * <p><strong>Nested Mapping:</strong></p>
 * <ul>
 *   <li>Uses ShiftMapper for nested shift definition</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        uses = {ShiftMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeShiftMapper {

    /**
     * Converts EmployeeShiftCreateDto to EmployeeShift entity.
     *
     * @param dto the create DTO
     * @return EmployeeShift entity
     */
    EmployeeShift toEntity(EmployeeShiftCreateDto dto);

    /**
     * Converts EmployeeShift entity to EmployeeShiftResponseDto.
     * Includes nested Shift definition.
     *
     * @param entity the EmployeeShift entity
     * @return EmployeeShiftResponseDto
     */
    EmployeeShiftResponseDto toResponseDto(EmployeeShift entity);

    /**
     * Converts list of EmployeeShift entities to list of DTOs.
     *
     * @param entities list of EmployeeShift entities
     * @return list of EmployeeShiftResponseDto
     */
    List<EmployeeShiftResponseDto> toResponseDtoList(List<EmployeeShift> entities);

    /**
     * Updates existing EmployeeShift entity from UpdateDto.
     * Null values in DTO are ignored.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    void updateEntity(EmployeeShiftUpdateDto dto, @MappingTarget EmployeeShift entity);
}
