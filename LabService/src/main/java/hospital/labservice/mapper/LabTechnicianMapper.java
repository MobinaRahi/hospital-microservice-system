package hospital.labservice.mapper;

import hospital.labservice.dto.labtechnician.LabTechnicianCreateDto;
import hospital.labservice.dto.labtechnician.LabTechnicianResponseDto;
import hospital.labservice.dto.labtechnician.LabTechnicianUpdateDto;
import hospital.labservice.model.LabTechnician;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for LabTechnician entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps all fields (userId, firstName, lastName, employeeCode,
 *       specialization, certificationNumber, shift, hireDate)</li>
 *   <li>Entity → ResponseDto: Maps all fields including createdAt, updatedAt</li>
 *   <li>UpdateDto → Entity: Partial update (null values ignored, identity fields not updated)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabTechnicianMapper {

    /**
     * Converts LabTechnicianCreateDto to LabTechnician entity.
     * isActive defaults to true (set by entity).
     *
     * @param dto the create DTO
     * @return LabTechnician entity
     */
    LabTechnician toEntity(LabTechnicianCreateDto dto);

    /**
     * Converts LabTechnician entity to LabTechnicianResponseDto.
     *
     * @param entity the LabTechnician entity
     * @return LabTechnicianResponseDto
     */
    LabTechnicianResponseDto toResponseDto(LabTechnician entity);

    /**
     * Converts list of LabTechnician entities to list of DTOs.
     *
     * @param entities list of LabTechnician entities
     * @return list of LabTechnicianResponseDto
     */
    List<LabTechnicianResponseDto> toResponseDtoList(List<LabTechnician> entities);

    /**
     * Updates existing LabTechnician entity from UpdateDto.
     * Null values in DTO are ignored.
     * Identity fields (userId, firstName, lastName, employeeCode, hireDate)
     * and audit fields are not updated.
     *
     * @param dto    the update DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(LabTechnicianUpdateDto dto, @MappingTarget LabTechnician entity);
}
