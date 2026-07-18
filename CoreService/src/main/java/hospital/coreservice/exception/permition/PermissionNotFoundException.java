package hospital.coreservice.exception.permition;

/**
 * Thrown when a permition is not found.
 *
 * @author Mobina
 */
public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(Long id) {
        super("Permission not found with id: " + id);
    }

    public PermissionNotFoundException(String name) {
        super("Permission not found with name: " + name);
    }

    public PermissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}