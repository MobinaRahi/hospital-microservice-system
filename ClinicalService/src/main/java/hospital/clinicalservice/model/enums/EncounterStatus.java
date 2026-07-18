package hospital.clinicalservice.model.enums;

/**
 * Status of an encounter (visit).
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * IN_PROGRESS → COMPLETED
 *      ↓
 *  CANCELLED
 * </pre>
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>IN_PROGRESS: Doctor is currently seeing the patient</li>
 *   <li>COMPLETED: Visit finished, all data recorded</li>
 *   <li>CANCELLED: Visit was cancelled (patient no-show, etc.)</li>
 * </ul>
 *
 * @author Mobina
 */
public enum EncounterStatus {
    /** Doctor is currently seeing the patient */
    IN_PROGRESS,

    /** Visit finished, all data recorded */
    COMPLETED,

    /** Visit was cancelled */
    CANCELLED
}
