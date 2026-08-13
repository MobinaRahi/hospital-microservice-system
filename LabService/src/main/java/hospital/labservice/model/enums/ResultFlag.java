package hospital.labservice.model.enums;

/**
 * Flags for laboratory test results indicating whether values are within normal range.
 *
 * <p><strong>Flag Order (severity):</strong></p>
 * <ul>
 *   <li>{@code NORMAL} - Within normal range</li>
 *   <li>{@code LOW} - Below normal range</li>
 *   <li>{@code HIGH} - Above normal range</li>
 *   <li>{@code CRITICAL_LOW} - Dangerously below normal range</li>
 *   <li>{@code CRITICAL_HIGH} - Dangerously above normal range</li>
 *   <li>{@code ABNORMAL} - Not within normal range (general)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public enum ResultFlag {
    NORMAL,
    LOW,
    HIGH,
    CRITICAL_LOW,
    CRITICAL_HIGH,
    ABNORMAL
}
