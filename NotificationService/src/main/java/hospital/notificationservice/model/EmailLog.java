package hospital.notificationservice.model;

import hospital.notificationservice.model.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents an email sent through the email service.
 * Tracks the complete lifecycle of an email from creation to delivery/opening.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → SENT → DELIVERED → OPENED
 *         ↓      ↓
 *     FAILED   FAILED
 * </pre>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each email has a recipient address, subject, and body</li>
 *   <li>TemplateId links to NotificationTemplate for reusable content</li>
 *   <li>Error messages are stored when sending fails</li>
 *   <li>OPENED status requires email tracking pixel support</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "email_logs",
        indexes = {
                @Index(name = "idx_email_status", columnList = "status"),
                @Index(name = "idx_email_to", columnList = "recipient_to"),
                @Index(name = "idx_email_sent_at", columnList = "sentAt"),
                @Index(name = "idx_email_template_id", columnList = "template_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmailLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recipient email address.
     * Example: "patient@example.com"
     */
    @Column(name = "recipient_to", nullable = false, length = 255)
    private String to;

    /**
     * Email subject line.
     */
    @Column(nullable = false, length = 500)
    private String subject;

    /**
     * Email body content.
     * Can be plain text or HTML.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    /**
     * ID of the notification template used (if any).
     * Links to NotificationTemplate for reusable content.
     */
    @Column(name = "template_id")
    private Long templateId;

    /**
     * Current status of the email in its lifecycle.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EmailStatus status = EmailStatus.PENDING;

    /**
     * Date and time when the email was sent.
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    /**
     * Error message if sending or delivery failed.
     * Example: "Invalid email address", "SMTP connection refused"
     */
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if the email is pending (waiting to be sent).
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return this.status == EmailStatus.PENDING;
    }

    /**
     * Checks if the email has been sent to the mail server.
     *
     * @return true if status is SENT or later
     */
    public boolean isSent() {
        return this.status != EmailStatus.PENDING && this.sentAt != null;
    }

    /**
     * Checks if the email has been delivered to the recipient's mailbox.
     *
     * @return true if status is DELIVERED
     */
    public boolean isDelivered() {
        return this.status == EmailStatus.DELIVERED;
    }

    /**
     * Checks if the email failed to send or deliver.
     *
     * @return true if status is FAILED
     */
    public boolean isFailed() {
        return this.status == EmailStatus.FAILED;
    }

    /**
     * Checks if the recipient has opened the email.
     *
     * @return true if status is OPENED
     */
    public boolean isOpened() {
        return this.status == EmailStatus.OPENED;
    }

    /**
     * Marks the email as sent.
     * Sets status to SENT and records the sent timestamp.
     */
    public void markSent() {
        this.status = EmailStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Marks the email as delivered.
     * Sets status to DELIVERED.
     */
    public void markDelivered() {
        this.status = EmailStatus.DELIVERED;
    }

    /**
     * Marks the email as opened by the recipient.
     * Sets status to OPENED.
     */
    public void markOpened() {
        this.status = EmailStatus.OPENED;
    }

    /**
     * Marks the email as failed.
     * Sets status to FAILED and stores the error message.
     *
     * @param errorMessage the error message describing the failure
     */
    public void markFailed(String errorMessage) {
        this.status = EmailStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    /**
     * Cancels the email.
     * Can only cancel PENDING emails.
     *
     * @throws IllegalStateException if email is not in PENDING status
     */
    public void cancel() {
        if (this.status != EmailStatus.PENDING) {
            throw new IllegalStateException("Can only cancel PENDING emails. Current status: " + this.status);
        }
        this.status = EmailStatus.FAILED;
        this.errorMessage = "Cancelled by user";
    }
}
