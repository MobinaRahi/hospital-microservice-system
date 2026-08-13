package hospital.labservice.model.enums;

/**
 * Status of a lab request throughout its lifecycle.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → APPROVED → SAMPLE_COLLECTED → IN_PROGRESS → COMPLETED
 *                                      ↓
 *                                  CANCELLED
 *                                  REJECTED
 * </pre>
 *
 * @author MobinaRahi
 */
public enum RequestStatus {
    PENDING,
    APPROVED,
    SAMPLE_COLLECTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    REJECTED
}
