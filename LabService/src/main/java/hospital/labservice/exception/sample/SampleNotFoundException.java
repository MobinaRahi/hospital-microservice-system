package hospital.labservice.exception.sample;

/**
 * Exception thrown when a sample is not found.
 *
 * @author MobinaRahi
 */
public class SampleNotFoundException extends RuntimeException {

    public SampleNotFoundException(String message) {
        super(message);
    }

    public static SampleNotFoundException byId(Long id) {
        return new SampleNotFoundException("Sample with id " + id + " not found");
    }

    public static SampleNotFoundException byNumber(String sampleNumber) {
        return new SampleNotFoundException("Sample with number '" + sampleNumber + "' not found");
    }
}
