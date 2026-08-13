package hospital.adminservice.exception.employeeshift;

public class EmployeeShiftNotFoundException extends RuntimeException {
    public EmployeeShiftNotFoundException(String message) { super(message); }
    public static EmployeeShiftNotFoundException byId(Long id) {
        return new EmployeeShiftNotFoundException("EmployeeShift with id " + id + " not found");
    }
}
