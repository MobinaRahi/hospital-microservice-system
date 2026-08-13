package hospital.labservice.exception.labtest;

/**
 * Exception thrown when a lab test is not found.
 *
 * @author MobinaRahi
 */
public class LabTestNotFoundException extends RuntimeException {

    public LabTestNotFoundException(String message) {
        super(message);
    }

    public static LabTestNotFoundException byId(Long id) {
        return new LabTestNotFoundException("Lab test with id " + id + " not found");
    }

    public static LabTestNotFoundException byCode(String code) {
        return new LabTestNotFoundException("Lab test with code '" + code + "' not found");
    }
}
