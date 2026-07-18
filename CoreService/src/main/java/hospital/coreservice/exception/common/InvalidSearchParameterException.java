package hospital.coreservice.exception.common;

/**
 * Thrown when an invalid common operation is attempted.
 *
 * @author Mobina
 */
public class InvalidSearchParameterException extends RuntimeException {
    public InvalidSearchParameterException(String fieldName) {
        super("Search parameter '" + fieldName + "' cannot be null or empty");
    }

}
