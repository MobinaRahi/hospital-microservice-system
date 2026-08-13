package hospital.labservice.exception.labrequestitem;

/**
 * Exception thrown when a lab request item is not found.
 *
 * @author MobinaRahi
 */
public class LabRequestItemNotFoundException extends RuntimeException {

    public LabRequestItemNotFoundException(String message) {
        super(message);
    }

    public static LabRequestItemNotFoundException byId(Long id) {
        return new LabRequestItemNotFoundException("Lab request item with id " + id + " not found");
    }
}
