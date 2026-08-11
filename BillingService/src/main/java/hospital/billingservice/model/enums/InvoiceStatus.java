package hospital.billingservice.model.enums;

/**
 * Status of an invoice throughout its lifecycle.
 *
 * <p><strong>Lifecycle:</strong></p>
 * <pre>
 * PENDING → PARTIAL → PAID
 *   ↑                   ↓
 *   └── REFUNDED ← CANCELLED
 *   OVERDUE (auto-detected based on dueDate)
 * </pre>
 *
 * @author MobinaRahi
 */
public enum InvoiceStatus {
    PENDING,
    PARTIAL,
    PAID,
    CANCELLED,
    REFUNDED,
    OVERDUE
}
