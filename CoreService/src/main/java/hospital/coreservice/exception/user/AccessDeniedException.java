package hospital.coreservice.exception.user;

/**
 * Exception for user operations.
 *
 * @author Mobina
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
