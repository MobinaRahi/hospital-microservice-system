package hospital.coreservice.exception.role;

public class DuplicateRoleException extends RuntimeException {
    public DuplicateRoleException(String name) {
        super("Role already exists with name: " + name);
    }
}
