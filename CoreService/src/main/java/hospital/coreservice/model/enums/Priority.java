package hospital.coreservice.model.enums;

/**
 * Priority level for appointments and queue entries.
 *
 * @author MobinaRahi
 */
public enum Priority {

    /** Normal priority (default). */
    NORMAL,

    /** Urgent priority (should be seen soon). */
    URGENT,

    /** Emergency priority (immediate attention). */
    EMERGENCY
}
