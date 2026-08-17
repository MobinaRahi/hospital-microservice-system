package hospital.adminservice.dto.systemlog;

import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new system log entry.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogCreateDto {

    @NotNull(message = "Log level is required")
    private LogLevel level;

    @NotNull(message = "Log category is required")
    private LogCategory category;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 5000, message = "Message must be at most 5000 characters")
    private String message;

    private Long userId;
    private Long relatedTenantId;
    private String ipAddress;
    private String metadata;
}
