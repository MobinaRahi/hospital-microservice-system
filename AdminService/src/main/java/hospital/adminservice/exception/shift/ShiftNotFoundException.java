package hospital.adminservice.exception.shift;

public class ShiftNotFoundException extends RuntimeException {
    public ShiftNotFoundException(String message) { super(message); }
    public static ShiftNotFoundException byId(Long id) {
        return new ShiftNotFoundException("Shift with id " + id + " not found");
    }
    public static ShiftNotFoundException byCode(String code) {
        return new ShiftNotFoundException("Shift with code '" + code + "' not found");
    }
}
