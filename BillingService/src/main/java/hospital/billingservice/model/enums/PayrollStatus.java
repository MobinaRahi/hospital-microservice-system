package hospital.billingservice.model.enums;

/**
 * Status of a payroll record throughout its processing lifecycle.
 *
 * <p><strong>Lifecycle:</strong></p>
 * <pre>
 * PENDING → PROCESSED → PAID
 *                    ↓
 *                CANCELLED
 * </pre>
 *
 * @author MobinaRahi
 */
public enum PayrollStatus {
    PENDING,
    PROCESSED,
    PAID,
    CANCELLED
}
