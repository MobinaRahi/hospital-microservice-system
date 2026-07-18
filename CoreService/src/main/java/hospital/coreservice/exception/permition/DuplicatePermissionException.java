package hospital.coreservice.exception.permition;

/**
 * Thrown when attempting to create a duplicate permition.
 *
 * @author Mobina
 */
public class DuplicatePermissionException extends RuntimeException {
    public DuplicatePermissionException(String name) {
        super("Permission already exists with name: " + name);
    }
}