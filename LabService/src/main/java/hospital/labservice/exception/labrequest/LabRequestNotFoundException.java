package hospital.labservice.exception.labrequest;

/**
 * Exception thrown when a lab request is not found.
 *
 * @author MobinaRahi
 */
public class LabRequestNotFoundException extends RuntimeException {

    public LabRequestNotFoundException(String message) {
        super(message);
    }

    public static LabRequestNotFoundException byId(Long id) {
        return new LabRequestNotFoundException("Lab request with id " + id + " not found");
    }

    public static LabRequestNotFoundException byNumber(String requestNumber) {
        return new LabRequestNotFoundException("Lab request with number '" + requestNumber + "' not found");
    }
}
