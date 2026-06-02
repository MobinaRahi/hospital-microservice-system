package hospital.coreservice.exception.shift;

public class DuplicateShiftNameException extends RuntimeException {
    public DuplicateShiftNameException(String shiftName) {
        super("Shift White Name " + shiftName + " already exists: ");
    }
}