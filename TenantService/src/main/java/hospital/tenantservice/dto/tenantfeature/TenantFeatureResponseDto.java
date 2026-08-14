package hospital.tenantservice.dto.tenantfeature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning tenant feature data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantFeatureResponseDto {

    private Long id;
    private Long tenantId;
    private String featureCode;
    private String featureName;
    private String description;
    private Boolean isActive;
    private Boolean isPlanFeature;
    private BigDecimal additionalCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
