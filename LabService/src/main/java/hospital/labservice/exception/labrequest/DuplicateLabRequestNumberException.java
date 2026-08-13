package hospital.labservice.exception.labrequest;

/**
 * Exception thrown when a duplicate lab request number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateLabRequestNumberException extends RuntimeException {

    public DuplicateLabRequestNumberException(String requestNumber) {
        super("Lab request with number '" + requestNumber + "' already exists");
    }
}
