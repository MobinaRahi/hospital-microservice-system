package hospital.clinicalservice.exception.prescription;

/**
 * Thrown when a prescription state transition is invalid.
 *
 * @author Mobina
 */
public class InvalidPrescriptionStateException extends RuntimeException {

    private InvalidPrescriptionStateException(String message) {
        super(message);
    }

    public static InvalidPrescriptionStateException alreadyCancelled(Long id) {
        return new InvalidPrescriptionStateException("Prescription " + id + " is already cancelled");
    }

    public static InvalidPrescriptionStateException alreadyExpired(Long id) {
        return new InvalidPrescriptionStateException("Prescription " + id + " is expired");
    }

    public static InvalidPrescriptionStateException cannotCancel(Long id, String currentStatus) {
        return new InvalidPrescriptionStateException("Prescription " + id + " cannot be cancelled from status: " + currentStatus);
    }
}
