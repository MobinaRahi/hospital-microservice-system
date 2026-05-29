package hospital.coreservice.exception.department;

public class DepartmentHasNoHeadDoctorException extends RuntimeException {
    public DepartmentHasNoHeadDoctorException(Long departmentId) {
        super("Department " + departmentId + " has no head doctor assigned");
    }
}
