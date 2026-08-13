package hospital.adminservice.model.enums;

/**
 * Current status of a hospital bed.
 * Tracks bed availability and current state.
 *
 * @author MobinaRahi
 */
public enum BedStatus {
    /** Bed is available for patient assignment */
    AVAILABLE,

    /** Bed is currently occupied by a patient */
    OCCUPIED,

    /** Bed is reserved for a future admission */
    RESERVED,

    /** Bed is under maintenance and cannot be used */
    MAINTENANCE,

    /** Bed is being cleaned and sanitized */
    CLEANING
}
