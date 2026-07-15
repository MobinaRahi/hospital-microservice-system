package hospital.clinicalservice.exception.allergy;

public class AllergyNotFoundException extends RuntimeException {

    private AllergyNotFoundException(String message) {
        super(message);
    }

    public static AllergyNotFoundException byId(Long id) {
        return new AllergyNotFoundException("Allergy with id " + id + " not found");
    }

    public static AllergyNotFoundException byPatientId(Long patientId) {
        return new AllergyNotFoundException("Allergy for patient " + patientId + " not found");
    }
}
