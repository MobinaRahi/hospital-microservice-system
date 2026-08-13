package hospital.labservice.exception.labresult;

/**
 * Exception thrown when a lab result is not found.
 *
 * @author MobinaRahi
 */
public class LabResultNotFoundException extends RuntimeException {

    public LabResultNotFoundException(String message) {
        super(message);
    }

    public static LabResultNotFoundException byId(Long id) {
        return new LabResultNotFoundException("Lab result with id " + id + " not found");
    }

    public static LabResultNotFoundException byRequestItemId(Long requestItemId) {
        return new LabResultNotFoundException("Lab result for request item " + requestItemId + " not found");
    }
}
