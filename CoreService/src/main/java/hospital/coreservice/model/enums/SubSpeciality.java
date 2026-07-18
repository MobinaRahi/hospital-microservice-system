package hospital.coreservice.model.enums;

/**
 * Doctor sub-specializations.
 * Used for more specific doctor categorization beyond main specialty.
 *
 * @author Mobina
 */
public enum SubSpeciality {
    /** Interventional cardiology procedures */
    INTERVENTIONAL_CARDIOLOGY,
    /** Echocardiography imaging */
    ECHOCARDIOGRAPHY,
    /** Heart rhythm disorders */
    ELECTROPHYSIOLOGY,
    /** Digestive system disorders */
    GASTROENTEROLOGY,
    /** Liver diseases */
    HEPATOLOGY,
    /** Kidney diseases */
    NEPHROLOGY,
    /** Stroke treatment */
    STROKE,
    /** Seizure disorders */
    EPILEPSY,
    /** Movement disorders (Parkinson's, etc.) */
    MOVEMENT_DISORDERS,
    /** No sub-specialty */
    NONE
}