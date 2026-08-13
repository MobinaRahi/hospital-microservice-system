package hospital.notificationservice.model.enums;

/**
 * Type of notification template.
 *
 * <p>Determines the channel through which the notification will be sent.</p>
 *
 * <p><strong>Types:</strong></p>
 * <ul>
 *   <li>{@code SMS} — Template for SMS notifications</li>
 *   <li>{@code EMAIL} — Template for email notifications</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum TemplateType {

    /** Template for SMS notifications. */
    SMS,

    /** Template for email notifications. */
    EMAIL
}
