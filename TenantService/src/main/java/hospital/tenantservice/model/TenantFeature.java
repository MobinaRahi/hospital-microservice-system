package hospital.tenantservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a feature flag for a specific tenant.
 *
 * <p>Each tenant can have multiple features enabled or disabled.
 * Features are tied to the subscription plan but can be customized
 * by the Super Admin.</p>
 *
 * <p><strong>Example Features:</strong></p>
 * <ul>
 *   <li>MODULE_BILLING - Billing/invoicing module</li>
 *   <li>MODULE_LAB - Laboratory module</li>
 *   <li>MODULE_INVENTORY - Inventory/pharmacy module</li>
 *   <li>MODULE_REPORTING - Advanced reporting</li>
 *   <li>MODULE_API - API access</li>
 *   <li>FEATURE_MULTI_LANGUAGE - Multi-language support</li>
 *   <li>FEATURE_CUSTOM_BRANDING - Custom branding/white-label</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "tenant_features",
        indexes = {
                @Index(name = "idx_tenant_feature_tenant", columnList = "tenantId"),
                @Index(name = "idx_tenant_feature_code", columnList = "featureCode"),
                @Index(name = "idx_tenant_feature_active", columnList = "isActive")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantFeature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tenant ID this feature belongs to.
     */
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    /**
     * Feature code identifier.
     * Example: "MODULE_BILLING", "FEATURE_API_ACCESS"
     */
    @Column(name = "feature_code", nullable = false, length = 100)
    private String featureCode;

    /**
     * Human-readable feature name.
     * Example: "Billing Module", "API Access"
     */
    @Column(nullable = false, length = 200)
    private String featureName;

    /**
     * Feature description.
     */
    @Column(length = 500)
    private String description;

    /**
     * Whether this feature is enabled for the tenant.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Whether this feature is included in the base plan (vs. add-on).
     */
    @Column(name = "is_plan_feature", nullable = false)
    @Builder.Default
    private Boolean isPlanFeature = true;

    /**
     * Additional cost for this feature (if add-on).
     * Null if included in plan.
     */
    @Column(name = "additional_cost", precision = 10, scale = 2)
    private java.math.BigDecimal additionalCost;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Enables this feature.
     */
    public void enable() {
        this.isActive = true;
    }

    /**
     * Disables this feature.
     */
    public void disable() {
        this.isActive = false;
    }

    /**
     * Checks if this feature is active.
     *
     * @return true if the feature is enabled
     */
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Checks if this is a plan-included feature (not an add-on).
     *
     * @return true if included in base plan
     */
    public boolean isIncludedInPlan() {
        return Boolean.TRUE.equals(this.isPlanFeature);
    }

    /**
     * Checks if this is an add-on feature with extra cost.
     *
     * @return true if it's a paid add-on
     */
    public boolean isAddOn() {
        return !Boolean.TRUE.equals(this.isPlanFeature) && this.additionalCost != null;
    }
}
