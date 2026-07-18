package hospital.coreservice.exception.user;

/**
 * Thrown when attempting to create a duplicate user.
 *
 * @author Mobina
 */
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username '" + username + "' is already taken");
    }
}