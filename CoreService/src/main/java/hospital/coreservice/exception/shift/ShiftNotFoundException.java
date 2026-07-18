package hospital.coreservice.exception.shift;

/**
 * Thrown when a shift is not found.
 *
 * @author Mobina
 */
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
