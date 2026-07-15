package hospital.clinicalservice.exception.nursingnote;

public class NursingNoteNotFoundException extends RuntimeException {

    private NursingNoteNotFoundException(String message) {
        super(message);
    }

    public static NursingNoteNotFoundException byId(Long id) {
        return new NursingNoteNotFoundException("Nursing note with id " + id + " not found");
    }

    public static NursingNoteNotFoundException byPatientId(Long patientId) {
        return new NursingNoteNotFoundException("Nursing note with patientId " + patientId + " not found");
    }
}
