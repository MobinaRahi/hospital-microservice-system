package hospital.adminservice.dto.systemconfig;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating an existing system configuration.
 * Config key and data type cannot be changed after creation.
 *
 * <p><strong>Updatable Fields:</strong></p>
 * <ul>
 *   <li>{@code configValue} - The configuration value</li>
 *   <li>{@code category} - Category for grouping</li>
 *   <li>{@code description} - Human-readable description</li>
 *   <li>{@code isEditable} - Edit permission flag</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
public class SystemConfigUpdateDto {

    private String configValue;

    @Size(max = 100, message = "Category must be at most 100 characters")
    private String category;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private Boolean isEditable;
}
