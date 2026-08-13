package hospital.labservice.exception.labtechnician;

/**
 * Exception thrown when a duplicate technician user ID is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateLabTechnicianUserIdException extends RuntimeException {

    public DuplicateLabTechnicianUserIdException(Long userId) {
        super("Lab technician with user id " + userId + " already exists");
    }
}
