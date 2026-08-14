package hospital.tenantservice.model;

import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tracks subscription changes for audit and billing purposes.
 *
 * <p>Every plan change, activation, suspension, or cancellation
 * is recorded here for historical tracking.</p>
 *
 * <p><strong>Change Types:</strong></p>
 * <ul>
 *   <li>{@code CREATED} - New tenant registration</li>
 *   <li>{@code UPGRADED} - Plan upgrade</li>
 *   <li>{@code DOWNGRADED} - Plan downgrade</li>
 *   <li>{@code ACTIVATED} - Tenant activated</li>
 *   <li>{@code SUSPENDED} - Tenant suspended</li>
 *   <li>{@code REACTIVATED} - Tenant reactivated after suspension</li>
 *   <li>{@code CANCELLED} - Subscription cancelled</li>
 *   <li>{@code EXPIRED} - Subscription expired</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "subscription_history",
        indexes = {
                @Index(name = "idx_sub_hist_tenant", columnList = "tenantId"),
                @Index(name = "idx_sub_hist_type", columnList = "changeType"),
                @Index(name = "idx_sub_hist_date", columnList = "changeDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubscriptionHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tenant ID this history record belongs to.
     */
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    /**
     * Type of subscription change.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private SubscriptionChangeType changeType;

    /**
     * Previous plan type (null for initial creation).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_plan")
    private PlanType previousPlan;

    /**
     * New plan type after the change.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "new_plan", nullable = false)
    private PlanType newPlan;

    /**
     * Date when the change occurred.
     */
    @Column(name = "change_date", nullable = false)
    private LocalDate changeDate;

    /**
     * Effective date for the change (may differ from changeDate).
     * Example: downgrade scheduled for end of billing period.
     */
    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    /**
     * Price difference for this change.
     * Positive for upgrade, negative for downgrade.
     */
    @Column(name = "price_difference", precision = 10, scale = 2)
    private BigDecimal priceDifference;

    /**
     * User ID who made this change (admin or tenant admin).
     */
    @Column(name = "changed_by")
    private Long changedBy;

    /**
     * Reason or notes for the change.
     */
    @Column(length = 1000)
    private String reason;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if this is an upgrade.
     *
     * @return true if new plan is higher tier than previous
     */
    public boolean isUpgrade() {
        return this.changeType == SubscriptionChangeType.UPGRADED;
    }

    /**
     * Checks if this is a downgrade.
     *
     * @return true if new plan is lower tier than previous
     */
    public boolean isDowngrade() {
        return this.changeType == SubscriptionChangeType.DOWNGRADED;
    }

    /**
     * Checks if this change has been applied (effective date is past).
     *
     * @return true if the change is effective
     */
    public boolean isEffective() {
        if (this.effectiveDate == null) return true;
        return !this.effectiveDate.isAfter(LocalDate.now());
    }

    /**
     * Checks if this change is scheduled for the future.
     *
     * @return true if effective date is in the future
     */
    public boolean isScheduled() {
        if (this.effectiveDate == null) return false;
        return this.effectiveDate.isAfter(LocalDate.now());
    }
}
