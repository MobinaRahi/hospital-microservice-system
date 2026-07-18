package hospital.coreservice.exception.user;

/**
 * Thrown when an invalid user operation is attempted.
 *
 * @author Mobina
 */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Current password is incorrect");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}