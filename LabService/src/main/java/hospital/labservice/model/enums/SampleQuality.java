package hospital.labservice.model.enums;

/**
 * Quality assessment of collected samples.
 * Used to flag samples that may produce unreliable results.
 *
 * @author MobinaRahi
 */
public enum SampleQuality {
    GOOD,
    HEMOLYZED,
    LIPEMIC,
    CLOTTED,
    INSUFFICIENT,
    CONTAMINATED
}
