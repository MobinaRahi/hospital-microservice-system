package com.hospital.superadmin.model;

import com.hospital.superadmin.model.enums.PlanType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a subscription plan in the SaaS platform.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each plan has a unique name</li>
 *   <li>Plans define limits for users, patients, appointments, and storage</li>
 *   <li>Plans have a monthly price and optional annual price</li>
 *   <li>isActive flag controls whether new tenants can subscribe to this plan</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "super_admin_plans",
        indexes = {
                @Index(name = "idx_plan_name", columnList = "name", unique = true),
                @Index(name = "idx_plan_type", columnList = "planType"),
                @Index(name = "idx_plan_active", columnList = "isActive")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique plan name.
     * Example: "Basic Plan", "Professional Plan"
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Plan type (FREE, BASIC, PROFESSIONAL, ENTERPRISE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    /**
     * Description of the plan.
     */
    @Column(length = 500)
    private String description;

    /**
     * Maximum number of users allowed.
     * -1 means unlimited.
     */
    @Column(name = "max_users", nullable = false)
    private Integer maxUsers;

    /**
     * Maximum number of patients allowed.
     * -1 means unlimited.
     */
    @Column(name = "max_patients", nullable = false)
    private Integer maxPatients;

    /**
     * Maximum number of appointments per month.
     * -1 means unlimited.
     */
    @Column(name = "max_appointments_per_month", nullable = false)
    private Integer maxAppointmentsPerMonth;

    /**
     * Storage limit in megabytes.
     * -1 means unlimited.
     */
    @Column(name = "storage_limit_mb", nullable = false)
    private Integer storageLimitMB;

    /**
     * Monthly price in local currency.
     * 0 for FREE plan.
     */
    @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    /**
     * Annual price (usually discounted).
     * Null if annual billing not available.
     */
    @Column(name = "annual_price", precision = 10, scale = 2)
    private BigDecimal annualPrice;

    /**
     * Support level included in this plan.
     * Example: "email", "priority", "24/7"
     */
    @Column(name = "support_level", length = 50)
    private String supportLevel;

    /**
     * Whether this plan is active and available for new subscriptions.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Sort order for display.
     * Lower number = displayed first.
     */
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if this plan has unlimited users.
     *
     * @return true if maxUsers is -1
     */
    public boolean hasUnlimitedUsers() {
        return this.maxUsers == -1;
    }

    /**
     * Checks if this plan has unlimited patients.
     *
     * @return true if maxPatients is -1
     */
    public boolean hasUnlimitedPatients() {
        return this.maxPatients == -1;
    }

    /**
     * Checks if this plan is free.
     *
     * @return true if monthlyPrice is 0
     */
    public boolean isFree() {
        return this.monthlyPrice.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Activates this plan.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates this plan.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Checks if this is an enterprise plan.
     *
     * @return true if planType is ENTERPRISE
     */
    public boolean isEnterprise() {
        return this.planType == PlanType.ENTERPRISE;
    }
}
