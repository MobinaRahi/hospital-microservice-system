package hospital.coreservice.exception.department;

/**
 * Exception for department operations.
 *
 * @author Mobina
 */
public class DepartmentHasNoHeadDoctorException extends RuntimeException {
    public DepartmentHasNoHeadDoctorException(Long departmentId) {
        super("Department " + departmentId + " has no head doctor assigned");
    }
}
