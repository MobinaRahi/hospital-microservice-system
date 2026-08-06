package hospital.inventoryservice.model.enums;

/**
 * Represents the pharmaceutical form of a drug.
 * Determines how the drug is administered to the patient.
 *
 * @author MobinaRahi
 */
public enum DrugForm {

    /**
     * Tablet - Solid oral dosage form
     */
    TABLET,

    /**
     * Capsule - Gelatin shell containing medication
     */
    CAPSULE,

    /**
     * Syrup - Liquid oral medication
     */
    SYRUP,

    /**
     * Injection - Injectable form (IV, IM, SC)
     */
    INJECTION,

    /**
     * Ointment - Semi-solid topical preparation
     */
    OINTMENT,

    /**
     * Drops - Liquid form for eyes, ears, or nose
     */
    DROPS,

    /**
     * Inhaler - For respiratory administration
     */
    INHALER,

    /**
     * Cream - Topical emulsion
     */
    CREAM,

    /**
     * Suppository - For rectal or vaginal administration
     */
    SUPPOSITORY
}
