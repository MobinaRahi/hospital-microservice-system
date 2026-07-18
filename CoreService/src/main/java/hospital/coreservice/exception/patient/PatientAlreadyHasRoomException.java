package hospital.coreservice.exception.patient;

/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientAlreadyHasRoomException extends RuntimeException {
    public PatientAlreadyHasRoomException(Long patientId, Long roomId) {
        super("Patient " + patientId + " already has a room (roomId: " + roomId + ")");
    }
}
