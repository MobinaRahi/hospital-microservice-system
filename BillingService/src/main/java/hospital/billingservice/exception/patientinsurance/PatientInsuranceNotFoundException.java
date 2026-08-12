package hospital.billingservice.exception.patientinsurance;

/**
 * Exception thrown when a patient insurance record is not found.
 *
 * @author MobinaRahi
 */
public class PatientInsuranceNotFoundException extends RuntimeException {

    public PatientInsuranceNotFoundException(String message) {
        super(message);
    }

    public static PatientInsuranceNotFoundException byId(Long id) {
        return new PatientInsuranceNotFoundException("Patient insurance with id " + id + " not found");
    }

    public static PatientInsuranceNotFoundException byPolicyNumber(String policyNumber) {
        return new PatientInsuranceNotFoundException("Patient insurance with policy number '" + policyNumber + "' not found");
    }
}
