package hospital.clinicalservice.exception.prescription;

/**
 * Thrown when a prescription item is not found.
 *
 * @author Mobina
 */
public class PrescriptionItemNotFoundException extends RuntimeException {

    private PrescriptionItemNotFoundException(String message) {
        super(message);
    }

    public static PrescriptionItemNotFoundException byId(Long id) {
        return new PrescriptionItemNotFoundException("Prescription item with id " + id + " not found");
    }

    public static PrescriptionItemNotFoundException byPrescriptionId(Long prescriptionId) {
        return new PrescriptionItemNotFoundException("Prescription item with prescriptionId " + prescriptionId + " not found");
    }
}
