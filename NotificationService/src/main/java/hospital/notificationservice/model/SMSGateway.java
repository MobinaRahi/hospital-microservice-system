package hospital.notificationservice.model;

import hospital.notificationservice.model.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents an SMS message sent through the SMS gateway.
 * Tracks the complete lifecycle of an SMS from creation to delivery.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → SENT → DELIVERED
 *         ↓      ↓
 *     FAILED   FAILED
 *     CANCELLED
 * </pre>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each SMS has a unique providerMessageId from the SMS gateway</li>
 *   <li>Cost is tracked per message for billing purposes</li>
 *   <li>Error messages are stored when sending fails</li>
 *   <li>TemplateId links to NotificationTemplate for reusable content</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "sms_gateways",
        indexes = {
                @Index(name = "idx_sms_status", columnList = "status"),
                @Index(name = "idx_sms_to", columnList = "to"),
                @Index(name = "idx_sms_sent_at", columnList = "sentAt"),
                @Index(name = "idx_sms_provider_msg_id", columnList = "providerMessageId")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SMSGateway extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recipient phone number.
     * Example: "+989123456789"
     */
    @Column(name = "recipient_to", nullable = false, length = 20)
    private String to;

    /**
     * SMS message content.
     * Can be plain text or template-rendered content.
     */
    @Column(nullable = false, length = 1600)
    private String message;

    /**
     * ID of the notification template used (if any).
     * Links to NotificationTemplate for reusable content.
     */
    @Column(name = "template_id")
    private Long templateId;

    /**
     * Current status of the SMS in its lifecycle.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SmsStatus status = SmsStatus.PENDING;

    /**
     * SMS gateway provider name.
     * Example: "Kavenegar", "Melipayamak", "Twilio"
     */
    @Column(length = 100)
    private String provider;

    /**
     * Unique message ID from the SMS gateway provider.
     * Used for tracking and delivery confirmation.
     */
    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;

    /**
     * Cost of sending this SMS in local currency.
     */
    @Column
    private Integer cost;

    /**
     * Date and time when the SMS was sent to the gateway.
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    /**
     * Date and time when the SMS was delivered to the recipient.
     * Null until delivery confirmation is received.
     */
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    /**
     * Error message if sending or delivery failed.
     * Example: "Invalid phone number", "Gateway timeout"
     */
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if the SMS is pending (waiting to be sent).
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return this.status == SmsStatus.PENDING;
    }

    /**
     * Checks if the SMS has been sent to the gateway.
     *
     * @return true if status is SENT or later
     */
    public boolean isSent() {
        return this.status != SmsStatus.PENDING && this.sentAt != null;
    }

    /**
     * Checks if the SMS has been delivered to the recipient.
     *
     * @return true if status is DELIVERED
     */
    public boolean isDelivered() {
        return this.status == SmsStatus.DELIVERED;
    }

    /**
     * Checks if the SMS failed to send or deliver.
     *
     * @return true if status is FAILED
     */
    public boolean isFailed() {
        return this.status == SmsStatus.FAILED;
    }

    /**
     * Checks if the SMS was cancelled.
     *
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return this.status == SmsStatus.CANCELLED;
    }

    /**
     * Marks the SMS as sent.
     * Sets status to SENT and records the sent timestamp.
     *
     * @param providerMessageId the message ID from the SMS gateway
     */
    public void markSent(String providerMessageId) {
        this.status = SmsStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.providerMessageId = providerMessageId;
    }

    /**
     * Marks the SMS as delivered.
     * Sets status to DELIVERED and records the delivery timestamp.
     */
    public void markDelivered() {
        this.status = SmsStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    /**
     * Marks the SMS as failed.
     * Sets status to FAILED and stores the error message.
     *
     * @param errorMessage the error message describing the failure
     */
    public void markFailed(String errorMessage) {
        this.status = SmsStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    /**
     * Cancels the SMS.
     * Can only cancel PENDING messages.
     *
     * @throws IllegalStateException if SMS is not in PENDING status
     */
    public void cancel() {
        if (this.status != SmsStatus.PENDING) {
            throw new IllegalStateException("Can only cancel PENDING SMS. Current status: " + this.status);
        }
        this.status = SmsStatus.CANCELLED;
    }

    /**
     * Calculates the delivery time in minutes.
     *
     * @return delivery time in minutes, or null if not yet delivered
     */
    public Long getDeliveryTimeMinutes() {
        if (this.deliveredAt == null || this.sentAt == null) {
            return null;
        }
        return java.time.Duration.between(this.sentAt, this.deliveredAt).toMinutes();
    }
}
