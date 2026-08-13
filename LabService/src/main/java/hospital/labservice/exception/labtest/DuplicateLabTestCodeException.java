package hospital.labservice.exception.labtest;

/**
 * Exception thrown when a duplicate lab test code is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateLabTestCodeException extends RuntimeException {

    public DuplicateLabTestCodeException(String code) {
        super("Lab test with code '" + code + "' already exists");
    }
}
