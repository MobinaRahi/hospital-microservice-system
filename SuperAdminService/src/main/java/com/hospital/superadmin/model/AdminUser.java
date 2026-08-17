package com.hospital.superadmin.model;

import com.hospital.superadmin.model.enums.AdminStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a super admin user in the SaaS platform.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each admin has a unique userId linked to AuthService</li>
 *   <li>Super admins have full access to all tenants</li>
 *   <li>Admin status can be ACTIVE, SUSPENDED, or INACTIVE</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "super_admin_users",
        indexes = {
                @Index(name = "idx_admin_user", columnList = "userId", unique = true),
                @Index(name = "idx_admin_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID from AuthService.
     * Links admin to their authentication account.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * Admin's full name.
     */
    @Column(nullable = false, length = 200)
    private String fullName;

    /**
     * Admin's email address.
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Admin's role/title.
     * Example: "System Administrator", "Billing Manager"
     */
    @Column(length = 100)
    private String role;

    /**
     * Admin status.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AdminStatus status = AdminStatus.ACTIVE;

    /**
     * Last login timestamp.
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * Last login IP address.
     */
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Records a successful login.
     *
     * @param ip the IP address of the login
     */
    public void recordLogin(String ip) {
        this.lastLoginAt = LocalDateTime.now();
        this.lastLoginIp = ip;
    }

    /**
     * Checks if the admin is active.
     *
     * @return true if status is ACTIVE
     */
    public boolean isActiveAdmin() {
        return this.status == AdminStatus.ACTIVE;
    }

    /**
     * Suspends this admin account.
     */
    public void suspend() {
        this.status = AdminStatus.SUSPENDED;
    }

    /**
     * Activates this admin account.
     */
    public void activate() {
        this.status = AdminStatus.ACTIVE;
    }

    /**
     * Deactivates this admin account.
     */
    public void deactivate() {
        this.status = AdminStatus.INACTIVE;
    }
}
