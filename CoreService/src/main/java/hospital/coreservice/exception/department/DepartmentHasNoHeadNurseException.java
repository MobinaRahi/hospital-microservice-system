package hospital.coreservice.exception.department;

/**
 * Exception for department operations.
 *
 * @author Mobina
 */
public class DepartmentHasNoHeadNurseException extends RuntimeException {
    public DepartmentHasNoHeadNurseException(Long departmentId) {
        super("Department " + departmentId + " has no head nurse assigned");
    }
}
