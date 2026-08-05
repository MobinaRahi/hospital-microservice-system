package hospital.authservice.model.tenant;

import hospital.authservice.model.enums.PlanType;
import hospital.authservice.model.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a tenant (hospital/clinic) in the SaaS platform.
 * Each tenant has isolated data and its own subscription.
 *
 * <p><strong>Multi-Tenancy:</strong></p>
 * <ul>
 *   <li>Each entity in the system stores a tenantId to isolate data</li>
 *   <li>Tenant is identified by subdomain (e.g., hospital1.novacare.com)</li>
 *   <li>Subscription controls access to modules and feature limits</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity(name = "tenantEntity")
@Table(name = "tenants",
        indexes = {
                @Index(name = "idx_tenant_subdomain", columnList = "subdomain", unique = true),
                @Index(name = "idx_tenant_status", columnList = "status"),
                @Index(name = "idx_tenant_plan", columnList = "plan_type")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_subdomain", columnNames = "subdomain")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Display name of the hospital/clinic.
     * Example: "City Hospital", "Clinic Mehr"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Unique subdomain for this tenant.
     * Example: "city-hospital" → city-hospital.novacare.com
     */
    @Column(nullable = false, unique = true, length = 100)
    private String subdomain;

    /**
     * Admin email for the tenant (contact person).
     */
    @Column(nullable = false, length = 200)
    private String adminEmail;

    /**
     * Phone number for the tenant.
     */
    @Column(length = 20)
    private String phone;

    /**
     * Physical address of the hospital/clinic.
     */
    @Column(length = 500)
    private String address;

    /**
     * Current subscription plan.
     */
    @Column(name = "plan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PlanType planType = PlanType.TRIAL;

    /**
     * Current lifecycle status of the tenant.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TenantStatus status = TenantStatus.TRIAL;

    /**
     * When the current subscription period started.
     */
    @Column(name = "subscription_start")
    private LocalDate subscriptionStart;

    /**
     * When the current subscription period ends.
     */
    @Column(name = "subscription_end")
    private LocalDate subscriptionEnd;

    /**
     * Maximum number of patients allowed per month based on plan.
     * null = unlimited (Enterprise).
     */
    @Column(name = "max_patients_per_month")
    private Integer maxPatientsPerMonth;

    /**
     * JSON string containing enabled module names.
     * Example: ["core","auth","clinical","inventory"]
     */
    @Column(name = "enabled_modules", length = 1000)
    private String enabledModules;

    /**
     * When this tenant was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last time tenant data was updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Whether this tenant record is soft-deleted.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the tenant's subscription is currently active.
     */
    public boolean isSubscriptionActive() {
        if (status != TenantStatus.ACTIVE && status != TenantStatus.TRIAL) {
            return false;
        }
        if (subscriptionEnd != null && subscriptionEnd.isBefore(LocalDate.now())) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a specific module is enabled for this tenant.
     */
    public boolean isModuleEnabled(String moduleName) {
        if (enabledModules == null || enabledModules.isBlank()) {
            return false;
        }
        return enabledModules.toLowerCase().contains(moduleName.toLowerCase());
    }
}
