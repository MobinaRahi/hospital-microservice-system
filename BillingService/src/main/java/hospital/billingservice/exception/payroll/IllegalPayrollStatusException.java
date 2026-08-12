package hospital.billingservice.exception.payroll;

/**
 * Exception thrown for illegal payroll status transitions.
 *
 * @author MobinaRahi
 */
public class IllegalPayrollStatusException extends RuntimeException {

    public IllegalPayrollStatusException(String message) {
        super(message);
    }

    public static IllegalPayrollStatusException cannotProcess(String currentStatus) {
        return new IllegalPayrollStatusException("Cannot process payroll in status: " + currentStatus);
    }

    public static IllegalPayrollStatusException cannotMarkPaid(String currentStatus) {
        return new IllegalPayrollStatusException("Cannot mark as paid from status: " + currentStatus);
    }

    public static IllegalPayrollStatusException cannotCancel(String currentStatus) {
        return new IllegalPayrollStatusException("Cannot cancel payroll in status: " + currentStatus);
    }
}
