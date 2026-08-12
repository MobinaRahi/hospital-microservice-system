package hospital.billingservice.exception.employee;

/**
 * Exception thrown when an employee is not found.
 *
 * @author MobinaRahi
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public static EmployeeNotFoundException byId(Long id) {
        return new EmployeeNotFoundException("Employee with id " + id + " not found");
    }

    public static EmployeeNotFoundException byCode(String employeeCode) {
        return new EmployeeNotFoundException("Employee with code '" + employeeCode + "' not found");
    }

    public static EmployeeNotFoundException byUserId(Long userId) {
        return new EmployeeNotFoundException("Employee with userId " + userId + " not found");
    }
}
