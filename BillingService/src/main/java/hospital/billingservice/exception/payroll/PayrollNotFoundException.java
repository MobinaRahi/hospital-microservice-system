package hospital.billingservice.exception.payroll;

/**
 * Exception thrown when a payroll record is not found.
 *
 * @author MobinaRahi
 */
public class PayrollNotFoundException extends RuntimeException {

    public PayrollNotFoundException(String message) {
        super(message);
    }

    public static PayrollNotFoundException byId(Long id) {
        return new PayrollNotFoundException("Payroll with id " + id + " not found");
    }

    public static PayrollNotFoundException byPeriod(Long employeeId, Integer month, Integer year) {
        return new PayrollNotFoundException("Payroll for employee " + employeeId + " in " + month + "/" + year + " not found");
    }
}
