package hospital.clinicalservice.model.enums;

/**
 * Types of encounters (visits).
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>OUTPATIENT: Patient visits clinic and goes home (most common)</li>
 *   <li>INPATIENT: Patient is admitted to hospital</li>
 *   <li>EMERGENCY: Urgent visit to ER</li>
 *   <li>FOLLOW_UP: Follow-up visit after previous encounter</li>
 * </ul>
 *
 * @author Mobina
 */
public enum EncounterType {
    /** Patient visits clinic and goes home */
    OUTPATIENT,

    /** Patient is admitted to hospital */
    INPATIENT,

    /** Urgent visit to ER */
    EMERGENCY,

    /** Follow-up visit after previous encounter */
    FOLLOW_UP
}
