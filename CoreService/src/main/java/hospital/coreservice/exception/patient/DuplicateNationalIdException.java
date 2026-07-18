package hospital.coreservice.exception.patient;

/**
 * Thrown when attempting to create a duplicate patient.
 *
 * @author Mobina
 */
public class DuplicateNationalIdException extends RuntimeException {
    public DuplicateNationalIdException( String nationalId) {
        super("nationalId already exists : "+ nationalId);
    }
}
