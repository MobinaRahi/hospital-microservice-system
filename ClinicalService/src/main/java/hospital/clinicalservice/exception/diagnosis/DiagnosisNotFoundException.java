package hospital.clinicalservice.exception.diagnosis;

public class DiagnosisNotFoundException extends RuntimeException {

    private DiagnosisNotFoundException(String message) {
        super(message);
    }

    public static DiagnosisNotFoundException byId(Long id) {
        return new DiagnosisNotFoundException("Diagnosis with id " + id + " not found");
    }

    public static DiagnosisNotFoundException byEncounterId(Long encounterId) {
        return new DiagnosisNotFoundException("Diagnosis with encounterId " + encounterId + " not found");
    }

    public static DiagnosisNotFoundException byIcd10Code(String icd10Code) {
        return new DiagnosisNotFoundException("Diagnosis with ICD-10 code " + icd10Code + " not found");
    }
}
