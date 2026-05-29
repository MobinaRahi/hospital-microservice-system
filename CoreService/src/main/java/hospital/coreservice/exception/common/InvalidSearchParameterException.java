package hospital.coreservice.exception.common;

public class InvalidSearchParameterException extends RuntimeException {
    public InvalidSearchParameterException(String fieldName) {
        super("Search parameter '" + fieldName + "' cannot be null or empty");
    }

}
