package hospital.adminservice.mapper;

import hospital.adminservice.dto.report.ReportCreateDto;
import hospital.adminservice.dto.report.ReportResponseDto;
import hospital.adminservice.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Report entity and DTOs.
 *
 * <p><strong>Mapping Strategy:</strong></p>
 * <ul>
 *   <li>CreateDto → Entity: Maps name, type, parameters</li>
 *   <li>Entity → ResponseDto: Maps all fields + computes status flags</li>
 *   <li>No UpdateDto - Reports are not updated after creation</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    /**
     * Converts ReportCreateDto to Report entity.
     *
     * @param dto the create DTO
     * @return Report entity
     */
    Report toEntity(ReportCreateDto dto);

    /**
     * Converts Report entity to ReportResponseDto.
     * Computed fields (completed, failed, processing) are set by service layer.
     *
     * @param entity the Report entity
     * @return ReportResponseDto
     */
    ReportResponseDto toResponseDto(Report entity);

    /**
     * Converts list of Report entities to list of DTOs.
     *
     * @param entities list of Report entities
     * @return list of ReportResponseDto
     */
    List<ReportResponseDto> toResponseDtoList(List<Report> entities);
}
