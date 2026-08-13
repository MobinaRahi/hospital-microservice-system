package hospital.notificationservice.model.enums;

/**
 * Status of an email in its lifecycle.
 *
 * <p><strong>Lifecycle:</strong></p>
 * <pre>
 * PENDING → SENT → DELIVERED → OPENED
 *         ↓      ↓
 *     FAILED   FAILED
 * </pre>
 *
 * <p><strong>Note:</strong></p>
 * <p>OPENED status requires email tracking pixel to be supported
 * by the recipient's email client.</p>
 *
 * <p><strong>Values:</strong></p>
 * <ul>
 *   <li>{@code PENDING} — Email is queued and waiting to be sent</li>
 *   <li>{@code SENT} — Email has been sent to the mail server</li>
 *   <li>{@code DELIVERED} — Email has been delivered to the recipient's mailbox</li>
 *   <li>{@code FAILED} — Email sending or delivery failed</li>
 *   <li>{@code OPENED} — Recipient has opened the email (requires tracking)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum EmailStatus {

    /** Email is queued and waiting to be sent. */
    PENDING,

    /** Email has been sent to the mail server. */
    SENT,

    /** Email has been delivered to the recipient's mailbox. */
    DELIVERED,

    /** Email sending or delivery failed. */
    FAILED,

    /** Recipient has opened the email (requires tracking pixel). */
    OPENED
}
