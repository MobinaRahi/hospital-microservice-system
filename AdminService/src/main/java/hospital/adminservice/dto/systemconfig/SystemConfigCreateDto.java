package hospital.adminservice.dto.systemconfig;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO for creating a new system configuration.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code configKey} - Unique configuration key (max 100 characters)</li>
 *   <li>{@code dataType} - Data type: STRING, INTEGER, BOOLEAN (max 50 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code configValue} - Configuration value</li>
 *   <li>{@code category} - Category for grouping (max 100 characters)</li>
 *   <li>{@code description} - Human-readable description (max 500 characters)</li>
 *   <li>{@code isEditable} - Whether can be edited via UI (default: true)</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <pre>
 * {
 *   "configKey": "MAX_APPOINTMENTS_PER_DAY",
 *   "configValue": "50",
 *   "category": "APPOINTMENT",
 *   "dataType": "INTEGER",
 *   "description": "Maximum number of appointments per day",
 *   "isEditable": true
 * }
 * </pre>
 *
 * @author MobinaRahi
 */
@Data
@SuperBuilder
public class SystemConfigCreateDto {

    @NotBlank(message = "Config key is required")
    @Size(max = 100, message = "Config key must be at most 100 characters")
    private String configKey;

    private String configValue;

    @Size(max = 100, message = "Category must be at most 100 characters")
    private String category;

    @NotBlank(message = "Data type is required")
    @Size(max = 50, message = "Data type must be at most 50 characters")
    private String dataType;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private Boolean isEditable;
}
