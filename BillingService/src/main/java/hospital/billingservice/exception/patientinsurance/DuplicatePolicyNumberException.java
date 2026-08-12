package hospital.billingservice.exception.patientinsurance;

/**
 * Exception thrown when a duplicate policy number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicatePolicyNumberException extends RuntimeException {

    public DuplicatePolicyNumberException(String policyNumber) {
        super("Patient insurance with policy number '" + policyNumber + "' already exists");
    }
}
