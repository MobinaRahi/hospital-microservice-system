package hospital.coreservice.exception.patient;

public class PatientAlreadyActiveException extends RuntimeException {
    public PatientAlreadyActiveException(Long id) {
        super("Patient with id " + id + " is already active");
    }
}
