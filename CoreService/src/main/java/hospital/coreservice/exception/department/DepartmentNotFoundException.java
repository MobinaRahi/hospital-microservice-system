package hospital.coreservice.exception.department;

/**
 * Thrown when a department is not found.
 *
 * @author Mobina
 */
public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
    public static DepartmentNotFoundException byId (Long id) {
        return new DepartmentNotFoundException("Department with id " + id + " not found");
    }
    public static DepartmentNotFoundException byCode (String code) {
        return new DepartmentNotFoundException("Department with code " + code + " not found");
    }
 public static DepartmentNotFoundException byName (String name) {
        return new DepartmentNotFoundException("Department with name " + name + " not found");
    }
}
