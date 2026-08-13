package hospital.adminservice.dto.systemconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for returning system configuration data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemConfigResponseDto {

    private Long id;
    private String configKey;
    private String configValue;
    private String category;
    private String dataType;
    private String description;
    private Boolean isEditable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
