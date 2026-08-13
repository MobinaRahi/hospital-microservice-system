package hospital.notificationservice.model;

import hospital.notificationservice.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents an in-app notification for a specific user.
 * These notifications are displayed within the application UI.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each notification is linked to a specific user via userId</li>
 *   <li>Notifications can be marked as read/unread</li>
 *   <li>type categorizes the notification for different handling logic</li>
 *   <li>relatedId links to the business entity (appointment, prescription, etc.)</li>
 * </ul>
 *
 * <p><strong>Notification Types:</strong></p>
 * <ul>
 *   <li>APPOINTMENT_REMINDER — Reminder for upcoming appointment</li>
 *   <li>APPOINTMENT_CANCELLED — Appointment cancellation notice</li>
 *   <li>PRESCRIPTION_READY — Prescription ready for pickup</li>
 *   <li>LAB_RESULT_READY — Lab result available</li>
 *   <li>BILLING — Invoice or payment notification</li>
 *   <li>SYSTEM — System-level notification</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "in_app_notifications",
        indexes = {
                @Index(name = "idx_notification_user", columnList = "userId"),
                @Index(name = "idx_notification_type", columnList = "type"),
                @Index(name = "idx_notification_is_read", columnList = "isRead"),
                @Index(name = "idx_notification_created", columnList = "createdAt"),
                @Index(name = "idx_notification_related", columnList = "relatedId")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InAppNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID from AuthService who should receive this notification.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Notification title.
     * Example: "Appointment Reminder", "Lab Result Ready"
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Notification message body.
     * Example: "Your appointment with Dr. Ali is tomorrow at 10:00 AM"
     */
    @Column(nullable = false, length = 1000)
    private String message;

    /**
     * Type of notification for categorization and handling logic.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    /**
     * ID of the related business entity.
     * Example: appointmentId, prescriptionId, labResultId, invoiceId
     */
    @Column(name = "related_id")
    private Long relatedId;

    /**
     * Whether the notification has been read by the user.
     */
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    /**
     * Date and time when the notification was marked as read.
     * Null if the notification hasn't been read yet.
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * Date and time when the notification was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Marks this notification as read.
     * Sets isRead=true and readAt=now.
     */
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Checks if the notification has been read.
     *
     * @return true if the notification is read
     */
    public boolean isReadNotification() {
        return Boolean.TRUE.equals(this.isRead);
    }

    /**
     * Checks if the notification is unread.
     *
     * @return true if the notification is unread
     */
    public boolean isUnread() {
        return !Boolean.TRUE.equals(this.isRead);
    }

    /**
     * Calculates the age of the notification in hours.
     * Used for determining if notification is recent.
     *
     * @return age in hours since creation
     */
    public Long getAgeInHours() {
        if (this.createdAt == null) {
            return null;
        }
        return java.time.Duration.between(this.createdAt, LocalDateTime.now()).toHours();
    }

    /**
     * Checks if the notification is recent (less than 24 hours old).
     *
     * @return true if notification is less than 24 hours old
     */
    public boolean isRecent() {
        Long age = getAgeInHours();
        return age != null && age < 24;
    }

    /**
     * Checks if this notification is of a specific type.
     *
     * @param type the notification type to check
     * @return true if the notification matches the given type
     */
    public boolean isType(NotificationType type) {
        return this.type == type;
    }

    /**
     * Checks if this notification is related to a specific entity.
     *
     * @param relatedId the related entity ID
     * @return true if the notification is related to the given entity
     */
    public boolean isRelatedTo(Long relatedId) {
        return this.relatedId != null && this.relatedId.equals(relatedId);
    }
}
