package hospital.coreservice.exception.role;

/**
 * Thrown when attempting to create a duplicate role.
 *
 * @author Mobina
 */
public class DuplicateRoleException extends RuntimeException {
    public DuplicateRoleException(String name) {
        super("Role already exists with name: " + name);
    }
}
