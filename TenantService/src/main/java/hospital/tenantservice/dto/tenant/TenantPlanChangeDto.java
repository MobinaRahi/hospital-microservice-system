package hospital.tenantservice.dto.tenant;

import hospital.tenantservice.model.enums.PlanType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for changing tenant subscription plan (upgrade/downgrade).
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantPlanChangeDto {

    @NotNull(message = "New plan is required")
    private PlanType newPlan;

    /**
     * Effective date for the change.
     * If null, change is immediate.
     * If in the future, change is scheduled.
     */
    private LocalDate effectiveDate;

    /**
     * Reason for the plan change.
     */
    private String reason;
}
