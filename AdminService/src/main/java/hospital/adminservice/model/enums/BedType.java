package hospital.adminservice.model.enums;

/**
 * Types of hospital beds available.
 * Each type may have different pricing and equipment.
 *
 * @author MobinaRahi
 */
public enum BedType {
    /** Standard general ward bed */
    GENERAL,

    /** Semi-private room bed (shared with one other patient) */
    SEMI_PRIVATE,

    /** Private room bed (single occupancy) */
    PRIVATE,

    /** VIP room bed with premium amenities */
    VIP,

    /** Intensive Care Unit bed */
    ICU,

    /** Coronary Care Unit bed */
    CCU,

    /** Neonatal Intensive Care Unit bed */
    NICU,

    /** Emergency department bed */
    EMERGENCY
}
