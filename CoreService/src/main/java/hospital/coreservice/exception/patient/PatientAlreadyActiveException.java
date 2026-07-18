package hospital.coreservice.exception.patient;

/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientAlreadyActiveException extends RuntimeException {
    public PatientAlreadyActiveException(Long id) {
        super("Patient with id " + id + " is already active");
    }
}
