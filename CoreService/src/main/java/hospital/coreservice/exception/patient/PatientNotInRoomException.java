package hospital.coreservice.exception.patient;

/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientNotInRoomException extends RuntimeException {
    public PatientNotInRoomException(Long patientId) {
        super("Patient " + patientId + " is not assigned to any room");
    }
}
