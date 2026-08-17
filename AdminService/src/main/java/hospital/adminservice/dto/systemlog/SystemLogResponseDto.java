package hospital.adminservice.dto.systemlog;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning system log data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemLogResponseDto {

    private Long id;
    private LogLevel level;
    private LogCategory category;
    private String title;
    private String message;
    private Long userId;
    private Long relatedTenantId;
    private String ipAddress;
    private String metadata;
    private Boolean isSevere;
    private LocalDateTime createdAt;
}
