package hospital.tenantservice.dto.tenantfeature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new tenant feature.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantFeatureCreateDto {

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotBlank(message = "Feature code is required")
    @Size(max = 100, message = "Feature code must be at most 100 characters")
    private String featureCode;

    @NotBlank(message = "Feature name is required")
    @Size(max = 200, message = "Feature name must be at most 200 characters")
    private String featureName;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    /**
     * Whether this feature is included in the base plan.
     * If false, it's an add-on with additional cost.
     */
    private Boolean isPlanFeature;

    /**
     * Additional monthly cost for add-on features.
     * Null for plan-included features.
     */
    private BigDecimal additionalCost;
}
