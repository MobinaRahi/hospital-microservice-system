package hospital.notificationservice.model.enums;

/**
 * Status of an SMS message in its lifecycle.
 *
 * <p><strong>Lifecycle:</strong></p>
 * <pre>
 * PENDING → SENT → DELIVERED
 *         ↓      ↓
 *     FAILED   FAILED
 *     CANCELLED
 * </pre>
 *
 * <p><strong>Values:</strong></p>
 * <ul>
 *   <li>{@code PENDING} — Message is queued and waiting to be sent</li>
 *   <li>{@code SENT} — Message has been sent to the SMS gateway</li>
 *   <li>{@code DELIVERED} — Message has been delivered to the recipient's device</li>
 *   <li>{@code FAILED} — Message sending or delivery failed</li>
 *   <li>{@code CANCELLED} — Message was cancelled before sending</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum SmsStatus {

    /** Message is queued and waiting to be sent. */
    PENDING,

    /** Message has been sent to the SMS gateway. */
    SENT,

    /** Message has been delivered to the recipient's device. */
    DELIVERED,

    /** Message sending or delivery failed. */
    FAILED,

    /** Message was cancelled before sending. */
    CANCELLED
}
