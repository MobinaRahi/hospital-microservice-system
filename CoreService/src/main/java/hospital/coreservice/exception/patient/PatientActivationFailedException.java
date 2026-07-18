package hospital.coreservice.exception.patient;

/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientActivationFailedException extends RuntimeException {
    public PatientActivationFailedException(Long id) {
        super("Failed to activate patient with id: " + id);
    }

    public PatientActivationFailedException(Long id, String reason) {
        super("Failed to activate patient with id: " + id + ". Reason: " + reason);
    }

    public PatientActivationFailedException(Long id, Throwable cause) {
        super("Failed to activate patient with id: " + id, cause);
    }
}