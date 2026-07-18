package hospital.coreservice.exception.user;

/**
 * Thrown when attempting to create a duplicate user.
 *
 * @author Mobina
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email '" + email + "' is already registered");
    }
}