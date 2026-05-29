package hospital.coreservice.exception.patient;

public class PatientAlreadyHasRoomException extends RuntimeException {
    public PatientAlreadyHasRoomException(Long patientId, Long roomId) {
        super("Patient " + patientId + " already has a room (roomId: " + roomId + ")");
    }
}
