package hospital.coreservice.exception.user;

/**
 * Thrown when a user is not found.
 *
 * @author Mobina
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(Long id) {
        return new UserNotFoundException("User with id " + id + " not found");
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("User with username '" + username + "' not found");
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("User with email '" + email + "' not found");
    }
}