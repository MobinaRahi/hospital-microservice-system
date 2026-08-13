package hospital.labservice.model.enums;

/**
 * Priority levels for lab requests.
 * Determines the urgency of processing.
 *
 * <p><strong>Priority Order:</strong></p>
 * <ul>
 *   <li>{@code STAT} - Emergency, process immediately</li>
 *   <li>{@code URGENT} - High priority, process within 1 hour</li>
 *   <li>{@code ROUTINE} - Normal priority, process within 24 hours</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum RequestPriority {
    ROUTINE,
    URGENT,
    STAT
}
