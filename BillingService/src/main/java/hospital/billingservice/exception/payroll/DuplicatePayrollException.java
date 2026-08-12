package hospital.billingservice.exception.payroll;

/**
 * Exception thrown when a duplicate payroll record for same employee/month/year is detected.
 *
 * @author MobinaRahi
 */
public class DuplicatePayrollException extends RuntimeException {

    public DuplicatePayrollException(Long employeeId, Integer month, Integer year) {
        super("Payroll for employee " + employeeId + " in " + month + "/" + year + " already exists");
    }
}
