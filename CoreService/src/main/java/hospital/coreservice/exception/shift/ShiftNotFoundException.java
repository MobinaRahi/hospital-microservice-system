package hospital.coreservice.exception.shift;

public class ShiftNotFoundException extends RuntimeException {
    public ShiftNotFoundException(String message) {
        super(message);
    }
    public static ShiftNotFoundException byId(Long shiftId) {
        return new ShiftNotFoundException("Shift with id " + shiftId + " not found");
    }

    public static ShiftNotFoundException byName(String name) {
        return new ShiftNotFoundException("Shift with name " + name + " not found");
    }
}
