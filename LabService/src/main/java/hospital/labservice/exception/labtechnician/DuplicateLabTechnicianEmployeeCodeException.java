package hospital.labservice.exception.labtechnician;

/**
 * Exception thrown when a duplicate technician employee code is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateLabTechnicianEmployeeCodeException extends RuntimeException {

    public DuplicateLabTechnicianEmployeeCodeException(String employeeCode) {
        super("Lab technician with employee code '" + employeeCode + "' already exists");
    }
}
