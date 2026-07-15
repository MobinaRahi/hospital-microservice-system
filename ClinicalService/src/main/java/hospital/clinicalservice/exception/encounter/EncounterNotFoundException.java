package hospital.clinicalservice.exception.encounter;

public class EncounterNotFoundException extends RuntimeException {

    private EncounterNotFoundException(String message) {
        super(message);
    }

    public static EncounterNotFoundException byId(Long id) {
        return new EncounterNotFoundException("Encounter with id " + id + " not found");
    }

    public static EncounterNotFoundException byPatientId(Long patientId) {
        return new EncounterNotFoundException("Encounter with patientId " + patientId + " not found");
    }

    public static EncounterNotFoundException byAppointmentId(Long appointmentId) {
        return new EncounterNotFoundException("Encounter with appointmentId " + appointmentId + " not found");
    }
}
