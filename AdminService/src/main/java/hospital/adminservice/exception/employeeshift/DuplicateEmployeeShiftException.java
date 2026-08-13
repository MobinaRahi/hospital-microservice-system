package hospital.adminservice.exception.employeeshift;

public class DuplicateEmployeeShiftException extends RuntimeException {
    public DuplicateEmployeeShiftException(Long employeeId, String date) {
        super("Employee " + employeeId + " already has a shift assigned on " + date);
    }
}
