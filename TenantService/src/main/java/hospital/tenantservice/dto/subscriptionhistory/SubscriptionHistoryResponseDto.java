package hospital.tenantservice.dto.subscriptionhistory;

import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning subscription history data in API responses.
 *
 * <p>Used for audit trail and billing history.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionHistoryResponseDto {

    private Long id;
    private Long tenantId;
    private SubscriptionChangeType changeType;
    private PlanType previousPlan;
    private PlanType newPlan;
    private LocalDate changeDate;
    private LocalDate effectiveDate;
    private BigDecimal priceDifference;
    private Long changedBy;
    private String reason;
    private LocalDateTime createdAt;

    // Computed Fields
    private Boolean isEffective;
    private Boolean isScheduled;
}
