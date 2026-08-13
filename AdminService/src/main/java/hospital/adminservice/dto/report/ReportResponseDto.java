package hospital.adminservice.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.adminservice.model.enums.ReportStatus;
import hospital.adminservice.model.enums.ReportType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for returning report data in API responses.
 * Includes computed status flags for convenience.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDto {

    private Long id;
    private String name;
    private ReportType type;
    private String parameters;
    private Long generatedBy;
    private LocalDateTime generatedAt;
    private String fileUrl;
    private ReportStatus status;

    /**
     * Computed flags based on status.
     */
    private Boolean completed;
    private Boolean failed;
    private Boolean processing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
