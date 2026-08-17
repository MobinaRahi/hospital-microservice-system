package hospital.adminservice.model;

import hospital.adminservice.model.enums.LogCategory;
import hospital.adminservice.model.enums.LogLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a system log entry for audit trail.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>System logs are read-only (no updates or deletes)</li>
 *   <li>Used for compliance and troubleshooting</li>
 *   <li>Categorized by level (INFO, WARN, ERROR, CRITICAL) and category</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "super_admin_logs",
        indexes = {
                @Index(name = "idx_log_level", columnList = "level"),
                @Index(name = "idx_log_category", columnList = "category"),
                @Index(name = "idx_log_created", columnList = "createdAt")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SystemLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Log severity level.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LogLevel level;

    /**
     * Log category.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LogCategory category;

    /**
     * Short title for the log entry.
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Detailed message.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * User ID who triggered this event.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * Tenant ID this log relates to.
     */
    @Column(name = "tenant_id")
    private Long relatedTenantId;

    /**
     * IP address of the request.
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * Additional metadata as JSON string.
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    /**
     * Checks if this is an error or critical log.
     *
     * @return true if level is ERROR or CRITICAL
     */
    public boolean isSevere() {
        return this.level == LogLevel.ERROR || this.level == LogLevel.CRITICAL;
    }
}
