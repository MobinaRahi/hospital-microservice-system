package hospital.tenantservice.model;

import hospital.tenantservice.model.enums.IndustryType;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a SaaS tenant (hospital or clinic) in the system.
 *
 * <p>Each tenant has its own subscription plan, usage limits, and lifecycle status.
 * Tenants are isolated from each other via the tenantId field in BaseEntity.</p>
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → ACTIVE → SUSPENDED → ACTIVE (reactivated)
 *                 ↓
 *               INACTIVE (cancelled/expired)
 * </pre>
 *
 * <p><strong>Plan Limits:</strong></p>
 * <ul>
 *   <li>FREE: 5 users, 50 patients, 100 appointments/month, 1GB storage</li>
 *   <li>BASIC: 20 users, 500 patients, 1,000 appointments/month, 10GB storage</li>
 *   <li>PROFESSIONAL: 100 users, 5,000 patients, 10,000 appointments/month, 100GB storage</li>
 *   <li>ENTERPRISE: unlimited everything</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "tenants",
        indexes = {
                @Index(name = "idx_tenant_subdomain", columnList = "subdomain", unique = true),
                @Index(name = "idx_tenant_status", columnList = "status"),
                @Index(name = "idx_tenant_plan", columnList = "plan"),
                @Index(name = "idx_tenant_active", columnList = "isActive")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the hospital or clinic.
     * Example: "Tehran Heart Hospital", "Shifa Clinic"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Unique subdomain for the tenant.
     * Used for subdomain-based tenant resolution.
     * Example: "tehran-heart", "shifa-clinic"
     */
    @Column(nullable = false, unique = true, length = 100)
    private String subdomain;

    /**
     * Admin email address for the tenant.
     * Used for account management and notifications.
     */
    @Column(name = "admin_email", nullable = false, length = 255)
    private String adminEmail;

    /**
     * Admin phone number for the tenant.
     */
    @Column(name = "admin_phone", length = 20)
    private String adminPhone;

    /**
     * Current subscription plan.
     * Determines usage limits and available features.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PlanType plan = PlanType.FREE;

    /**
     * Current lifecycle status of the tenant.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TenantStatus status = TenantStatus.PENDING;

    /**
     * Maximum number of users allowed by the current plan.
     * -1 means unlimited (ENTERPRISE).
     */
    @Column(name = "max_users", nullable = false)
    @Builder.Default
    private Integer maxUsers = 5;

    /**
     * Maximum number of patients allowed by the current plan.
     * -1 means unlimited (ENTERPRISE).
     */
    @Column(name = "max_patients", nullable = false)
    @Builder.Default
    private Integer maxPatients = 50;

    /**
     * Maximum number of appointments per month allowed by the plan.
     * -1 means unlimited (ENTERPRISE).
     */
    @Column(name = "max_appointments_per_month", nullable = false)
    @Builder.Default
    private Integer maxAppointmentsPerMonth = 100;

    /**
     * Storage limit in megabytes.
     * -1 means unlimited (ENTERPRISE).
     */
    @Column(name = "storage_limit_mb", nullable = false)
    @Builder.Default
    private Integer storageLimitMB = 1024; // 1GB

    /**
     * Support level for the tenant.
     * Example: "email", "priority", "24/7"
     */
    @Column(name = "support_level", length = 50)
    @Builder.Default
    private String supportLevel = "email";

    /**
     * Subscription start date.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Subscription end date (for time-limited plans).
     * Null for ENTERPRISE or perpetual plans.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Current number of registered users.
     * Used for plan limit enforcement.
     */
    @Column(name = "current_users", nullable = false)
    @Builder.Default
    private Integer currentUsers = 0;

    /**
     * Current number of registered patients.
     * Used for plan limit enforcement.
     */
    @Column(name = "current_patients", nullable = false)
    @Builder.Default
    private Integer currentPatients = 0;

    /**
     * Number of appointments in the current month.
     * Resets monthly for plan limit enforcement.
     */
    @Column(name = "current_month_appointments", nullable = false)
    @Builder.Default
    private Integer currentMonthAppointments = 0;

    /**
     * Address of the hospital/clinic.
     * Example: "123 Health Street, Medical District"
     */
    @Column(length = 500)
    private String address;

    /**
     * City where the tenant is located.
     */
    @Column(length = 100)
    private String city;

    /**
     * Country where the tenant is located.
     */
    @Column(length = 100)
    private String country;

    /**
     * General contact phone number.
     */
    @Column(length = 20)
    private String phone;

    /**
     * Tenant website URL.
     * Example: "https://tehran-heart.com"
     */
    @Column(length = 255)
    private String website;

    /**
     * Logo URL for tenant branding.
     */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /**
     * Industry type of the tenant.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private IndustryType industry;

    /**
     * Description or about text for the tenant.
     */
    @Column(length = 2000)
    private String description;

    /**
     * Tax number for billing/invoicing.
     */
    @Column(name = "tax_number", length = 50)
    private String taxNumber;

    /**
     * Timezone for scheduling.
     * Example: "Asia/Tehran", "UTC+3:30"
     */
    @Column(length = 50)
    @Builder.Default
    private String timezone = "UTC";

    /**
     * Whether the tenant is currently active and accessible.
     * Separated from status for quick boolean checks.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = false;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Activates the tenant.
     * Sets status=ACTIVE, isActive=true.
     * Can only activate PENDING or SUSPENDED tenants.
     *
     * @throws IllegalStateException if tenant is INACTIVE
     */
    public void activate() {
        if (this.status == TenantStatus.INACTIVE) {
            throw new IllegalStateException("Cannot activate an INACTIVE tenant. Status: " + this.status);
        }
        this.status = TenantStatus.ACTIVE;
        this.isActive = true;
    }

    /**
     * Deactivates the tenant.
     * Sets status=INACTIVE, isActive=false.
     */
    public void deactivate() {
        this.status = TenantStatus.INACTIVE;
        this.isActive = false;
    }

    /**
     * Suspends the tenant temporarily.
     * Sets status=SUSPENDED, isActive=false.
     * Used for payment issues or policy violations.
     *
     * @throws IllegalStateException if tenant is not ACTIVE
     */
    public void suspend() {
        if (this.status != TenantStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend ACTIVE tenants. Status: " + this.status);
        }
        this.status = TenantStatus.SUSPENDED;
        this.isActive = false;
    }

    /**
     * Upgrades the tenant to a higher plan.
     * Updates plan and all plan-specific limits.
     *
     * @param newPlan the new plan type
     */
    public void upgradePlan(PlanType newPlan) {
        applyPlanLimits(newPlan);
    }

    /**
     * Downgrades the tenant to a lower plan.
     * Verifies current usage doesn't exceed new limits before downgrading.
     *
     * @param newPlan the new plan type
     * @throws IllegalStateException if current usage exceeds new plan limits
     */
    public void downgradePlan(PlanType newPlan) {
        if (!canDowngradeTo(newPlan)) {
            throw new IllegalStateException(
                "Cannot downgrade to " + newPlan + ". Current usage exceeds new plan limits. " +
                "Users: " + currentUsers + "/" + getMaxUsersForPlan(newPlan) + ", " +
                "Patients: " + currentPatients + "/" + getMaxPatientsForPlan(newPlan)
            );
        }
        applyPlanLimits(newPlan);
    }

    /**
     * Checks if the tenant can add a new user based on plan limits.
     *
     * @return true if under the user limit
     */
    public boolean canAddUser() {
        if (maxUsers == -1) return true; // unlimited
        return currentUsers < maxUsers;
    }

    /**
     * Checks if the tenant can add a new patient based on plan limits.
     *
     * @return true if under the patient limit
     */
    public boolean canAddPatient() {
        if (maxPatients == -1) return true; // unlimited
        return currentPatients < maxPatients;
    }

    /**
     * Checks if the tenant can add a new appointment this month.
     *
     * @return true if under the appointment limit
     */
    public boolean canAddAppointment() {
        if (maxAppointmentsPerMonth == -1) return true; // unlimited
        return currentMonthAppointments < maxAppointmentsPerMonth;
    }

    /**
     * Increments the user count.
     *
     * @throws IllegalStateException if user limit is reached
     */
    public void incrementUsers() {
        if (!canAddUser()) {
            throw new IllegalStateException("User limit reached for plan " + plan + " (" + maxUsers + " users)");
        }
        currentUsers++;
    }

    /**
     * Increments the patient count.
     *
     * @throws IllegalStateException if patient limit is reached
     */
    public void incrementPatients() {
        if (!canAddPatient()) {
            throw new IllegalStateException("Patient limit reached for plan " + plan + " (" + maxPatients + " patients)");
        }
        currentPatients++;
    }

    /**
     * Increments the monthly appointment count.
     *
     * @throws IllegalStateException if appointment limit is reached
     */
    public void incrementAppointments() {
        if (!canAddAppointment()) {
            throw new IllegalStateException("Monthly appointment limit reached for plan " + plan + " (" + maxAppointmentsPerMonth + "/month)");
        }
        currentMonthAppointments++;
    }

    /**
     * Decrements the user count (e.g., when user is removed).
     */
    public void decrementUsers() {
        if (currentUsers > 0) currentUsers--;
    }

    /**
     * Decrements the patient count.
     */
    public void decrementPatients() {
        if (currentPatients > 0) currentPatients--;
    }

    /**
     * Checks if the subscription has expired.
     *
     * @return true if endDate is in the past
     */
    public boolean isExpired() {
        if (endDate == null) return false; // no expiry (ENTERPRISE)
        return endDate.isBefore(LocalDate.now());
    }

    /**
     * Checks if the tenant is operational (active and not expired).
     *
     * @return true if tenant is ACTIVE and not expired
     */
    public boolean isOperational() {
        return this.isActive && !isExpired() && this.status == TenantStatus.ACTIVE;
    }

    /**
     * Checks if the tenant has reached its storage limit.
     *
     * @param usedStorageMB current storage usage in MB
     * @return true if over the limit
     */
    public boolean isStorageFull(int usedStorageMB) {
        if (storageLimitMB == -1) return false; // unlimited
        return usedStorageMB >= storageLimitMB;
    }

    // ════════════════════════════════════════════════════════════════════
    // Private Helper Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Applies plan-specific limits based on the plan type.
     *
     * @param plan the plan to apply
     */
    private void applyPlanLimits(PlanType plan) {
        this.plan = plan;
        this.maxUsers = getMaxUsersForPlan(plan);
        this.maxPatients = getMaxPatientsForPlan(plan);
        this.maxAppointmentsPerMonth = getMaxAppointmentsForPlan(plan);
        this.storageLimitMB = getStorageForPlan(plan);
        this.supportLevel = getSupportForPlan(plan);
    }

    private boolean canDowngradeTo(PlanType newPlan) {
        return (newPlan == PlanType.ENTERPRISE ||
                currentUsers <= getMaxUsersForPlan(newPlan)) &&
               (newPlan == PlanType.ENTERPRISE ||
                currentPatients <= getMaxPatientsForPlan(newPlan));
    }

    private int getMaxUsersForPlan(PlanType plan) {
        return switch (plan) {
            case FREE -> 5;
            case BASIC -> 20;
            case PROFESSIONAL -> 100;
            case ENTERPRISE -> -1;
        };
    }

    private int getMaxPatientsForPlan(PlanType plan) {
        return switch (plan) {
            case FREE -> 50;
            case BASIC -> 500;
            case PROFESSIONAL -> 5000;
            case ENTERPRISE -> -1;
        };
    }

    private int getMaxAppointmentsForPlan(PlanType plan) {
        return switch (plan) {
            case FREE -> 100;
            case BASIC -> 1000;
            case PROFESSIONAL -> 10000;
            case ENTERPRISE -> -1;
        };
    }

    private int getStorageForPlan(PlanType plan) {
        return switch (plan) {
            case FREE -> 1024;         // 1GB
            case BASIC -> 10240;       // 10GB
            case PROFESSIONAL -> 102400; // 100GB
            case ENTERPRISE -> -1;     // unlimited
        };
    }

    private String getSupportForPlan(PlanType plan) {
        return switch (plan) {
            case FREE, BASIC -> "email";
            case PROFESSIONAL -> "priority";
            case ENTERPRISE -> "24/7";
        };
    }
}
