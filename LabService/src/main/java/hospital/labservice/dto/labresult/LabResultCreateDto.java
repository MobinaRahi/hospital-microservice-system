package hospital.labservice.dto.labresult;

import hospital.labservice.model.enums.ResultFlag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating a new lab result.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code requestItemId} - Lab request item ID</li>
 *   <li>{@code value} - Measured test value</li>
 *   <li>{@code performedAt} - Date/time when test was performed</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code normalRange} - Normal reference range</li>
 *   <li>{@code flag} - Result flag indicating if value is within normal range</li>
 *   <li>{@code unit} - Unit of measurement</li>
 *   <li>{@code performedBy} - User ID of technician who performed the test</li>
 *   <li>{@code notes} - Additional notes about the result</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultCreateDto {

    @NotNull(message = "Request item ID is required")
    private Long requestItemId;

    @NotBlank(message = "Result value is required")
    @Size(max = 100, message = "Result value must be at most 100 characters")
    private String value;

    @Size(max = 100, message = "Normal range must be at most 100 characters")
    private String normalRange;

    private ResultFlag flag;

    @Size(max = 50, message = "Unit must be at most 50 characters")
    private String unit;

    @NotNull(message = "Performed date is required")
    private LocalDateTime performedAt;

    private Long performedBy;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
