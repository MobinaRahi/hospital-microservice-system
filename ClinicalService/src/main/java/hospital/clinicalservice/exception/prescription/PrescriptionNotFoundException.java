package hospital.clinicalservice.exception.prescription;

/**
 * Thrown when a prescription is not found by ID or patient.
 *
 * @author Mobina
 */
public class PrescriptionNotFoundException extends RuntimeException {

    private PrescriptionNotFoundException(String message) {
        super(message);
    }

    public static PrescriptionNotFoundException byId(Long id) {
        return new PrescriptionNotFoundException("Prescription with id " + id + " not found");
    }

    public static PrescriptionNotFoundException byPatientId(Long patientId) {
        return new PrescriptionNotFoundException("Prescription with patientId " + patientId + " not found");
    }
}
