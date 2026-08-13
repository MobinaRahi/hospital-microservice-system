package hospital.labservice.exception.sample;

/**
 * Exception thrown when a duplicate sample number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateSampleNumberException extends RuntimeException {

    public DuplicateSampleNumberException(String sampleNumber) {
        super("Sample with number '" + sampleNumber + "' already exists");
    }
}
