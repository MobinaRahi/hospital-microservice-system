package hospital.adminservice.exception.shift;

public class DuplicateShiftCodeException extends RuntimeException {
    public DuplicateShiftCodeException(String code) {
        super("Shift with code '" + code + "' already exists");
    }
}
