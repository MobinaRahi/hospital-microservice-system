package hospital.coreservice.exception.department;

public class DepartmentHasNoHeadNurseException extends RuntimeException {
    public DepartmentHasNoHeadNurseException(Long departmentId) {
        super("Department " + departmentId + " has no head nurse assigned");
    }
}
