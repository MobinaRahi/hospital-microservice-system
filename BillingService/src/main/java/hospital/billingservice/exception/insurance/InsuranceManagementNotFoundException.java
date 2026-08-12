package hospital.billingservice.exception.insurance;

/**
 * Exception thrown when an insurance plan is not found.
 *
 * @author MobinaRahi
 */
public class InsuranceManagementNotFoundException extends RuntimeException {

    public InsuranceManagementNotFoundException(String message) {
        super(message);
    }

    public static InsuranceManagementNotFoundException byId(Long id) {
        return new InsuranceManagementNotFoundException("Insurance plan with id " + id + " not found");
    }

    public static InsuranceManagementNotFoundException byCode(String code) {
        return new InsuranceManagementNotFoundException("Insurance plan with code '" + code + "' not found");
    }
}
