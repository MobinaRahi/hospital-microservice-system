package hospital.clinicalservice.exception.observation;

public class ObservationNotFoundException extends RuntimeException {

    private ObservationNotFoundException(String message) {
        super(message);
    }

    public static ObservationNotFoundException byId(Long id) {
        return new ObservationNotFoundException("Observation with id " + id + " not found");
    }

    public static ObservationNotFoundException byPatientIdAndLoinc(Long patientId, String loincCode) {
        return new ObservationNotFoundException("Observation for patient " + patientId + " with LOINC " + loincCode + " not found");
    }
}
