package hospital.labservice.dto.labresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.model.enums.ResultFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning lab result data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabResultResponseDto {

    private Long id;
    private Long requestItemId;
    private String value;
    private String normalRange;
    private ResultFlag flag;
    private String unit;
    private LocalDateTime performedAt;
    private Long performedBy;
    private LocalDateTime verifiedAt;
    private Long verifiedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
