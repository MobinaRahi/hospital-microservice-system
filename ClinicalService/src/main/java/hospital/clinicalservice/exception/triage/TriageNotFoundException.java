package hospital.clinicalservice.exception.triage;

public class TriageNotFoundException extends RuntimeException {

    private TriageNotFoundException(String message) {
        super(message);
    }

    public static TriageNotFoundException byId(Long id) {
        return new TriageNotFoundException("Triage with id " + id + " not found");
    }

    public static TriageNotFoundException byPatientId(Long patientId) {
        return new TriageNotFoundException("Triage for patient " + patientId + " not found");
    }
}
