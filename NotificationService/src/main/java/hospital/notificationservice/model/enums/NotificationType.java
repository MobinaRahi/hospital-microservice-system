package hospital.notificationservice.model.enums;

/**
 * Type of in-app notification.
 *
 * <p>Used to categorize notifications by their business purpose,
 * allowing different handling logic for each type.</p>
 *
 * <p><strong>Types:</strong></p>
 * <ul>
 *   <li>{@code APPOINTMENT_REMINDER} — Reminder for upcoming appointment</li>
 *   <li>{@code APPOINTMENT_CANCELLED} — Notification about appointment cancellation</li>
 *   <li>{@code PRESCRIPTION_READY} — Prescription is ready for pickup</li>
 *   <li>{@code LAB_RESULT_READY} — Lab result is ready to view</li>
 *   <li>{@code BILLING} — Billing-related notification (invoice, payment)</li>
 *   <li>{@code SYSTEM} — System-level notification (maintenance, updates)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum NotificationType {

    /** Reminder for upcoming appointment. */
    APPOINTMENT_REMINDER,

    /** Notification about appointment cancellation. */
    APPOINTMENT_CANCELLED,

    /** Prescription is ready for pickup. */
    PRESCRIPTION_READY,

    /** Lab result is ready to view. */
    LAB_RESULT_READY,

    /** Billing-related notification (invoice, payment). */
    BILLING,

    /** System-level notification (maintenance, updates). */
    SYSTEM
}
