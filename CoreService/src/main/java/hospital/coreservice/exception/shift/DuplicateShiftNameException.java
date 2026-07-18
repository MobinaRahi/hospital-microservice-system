package hospital.coreservice.exception.shift;

/**
 * Thrown when attempting to create a duplicate shift.
 *
 * @author Mobina
 */
public class DuplicateShiftNameException extends RuntimeException {
    public DuplicateShiftNameException(String shiftName) {
        super("Shift White Name " + shiftName + " already exists: ");
    }
}