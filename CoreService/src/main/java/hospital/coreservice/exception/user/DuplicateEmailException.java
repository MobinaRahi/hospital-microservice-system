package hospital.coreservice.exception.user;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email '" + email + "' is already registered");
    }
}