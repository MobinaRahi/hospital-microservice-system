package hospital.adminservice.exception.employeeshift;

public class AlreadyMarkedException extends RuntimeException {
    public AlreadyMarkedException(Long id, String status) {
        super("EmployeeShift " + id + " is already marked as " + status);
    }
}
