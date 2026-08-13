package hospital.labservice.model.enums;

/**
 * Status of individual items within a lab request.
 * Each test in a request can have its own status.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *                      ↓
 *                  CANCELLED
 * </pre>
 *
 * @author MobinaRahi
 */
public enum RequestItemStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELLED
}
