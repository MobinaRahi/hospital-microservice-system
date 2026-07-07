package hospital.authservice.exception.user;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Current password is incorrect");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}