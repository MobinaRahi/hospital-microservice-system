package hospital.billingservice.exception.employee;

/**
 * Exception thrown when a duplicate employee code is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateEmployeeCodeException extends RuntimeException {

    public DuplicateEmployeeCodeException(String employeeCode) {
        super("Employee with code '" + employeeCode + "' already exists");
    }
}
