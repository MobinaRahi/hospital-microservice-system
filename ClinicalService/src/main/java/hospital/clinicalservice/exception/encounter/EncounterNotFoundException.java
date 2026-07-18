package hospital.clinicalservice.exception.encounter;

/**
 * Exception thrown when an encounter is not found.
 *
 * <p><strong>When thrown:</strong></p>
 * <ul>
 *   <li>byId: When looking up encounter by ID</li>
 *   <li>byPatientId: When looking up encounters for a patient</li>
 *   <li>byAppointmentId: When looking up encounters for an appointment</li>
 * </ul>
 *
 * <p><strong>HTTP Status:</strong> 404 Not Found</p>
 *
 * @author Mobina
 */
public class EncounterNotFoundException extends RuntimeException {

    private EncounterNotFoundException(String message) {
        super(message);
    }

    /**
     * Thrown when encounter not found by ID.
     * Example: GET /api/v1/encounters/999 → "Encounter with id 999 not found"
     */
    public static EncounterNotFoundException byId(Long id) {
        return new EncounterNotFoundException("Encounter with id " + id + " not found");
    }

    /**
     * Thrown when no encounters found for a patient.
     * Example: GET /api/v1/encounters/patient/123 → "Encounter with patientId 123 not found"
     */
    public static EncounterNotFoundException byPatientId(Long patientId) {
        return new EncounterNotFoundException("Encounter with patientId " + patientId + " not found");
    }

    /**
     * Thrown when no encounters found for an appointment.
     * Example: GET /api/v1/encounters/appointment/456 → "Encounter with appointmentId 456 not found"
     */
    public static EncounterNotFoundException byAppointmentId(Long appointmentId) {
        return new EncounterNotFoundException("Encounter with appointmentId " + appointmentId + " not found");
    }
}
