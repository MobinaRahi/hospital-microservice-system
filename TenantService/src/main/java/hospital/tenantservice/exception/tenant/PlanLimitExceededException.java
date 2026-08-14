package hospital.tenantservice.exception.tenant;

/**
 * Exception thrown when a tenant exceeds its plan limits.
 *
 * @author MobinaRahi
 */
public class PlanLimitExceededException extends RuntimeException {

    public PlanLimitExceededException(String message) {
        super(message);
    }

    public static PlanLimitExceededException userLimitReached(int current, int max) {
        return new PlanLimitExceededException("User limit reached: " + current + "/" + max);
    }

    public static PlanLimitExceededException patientLimitReached(int current, int max) {
        return new PlanLimitExceededException("Patient limit reached: " + current + "/" + max);
    }

    public static PlanLimitExceededException appointmentLimitReached(int current, int max) {
        return new PlanLimitExceededException("Monthly appointment limit reached: " + current + "/" + max);
    }

    public static PlanLimitExceededException storageLimitReached(int usedMB, int limitMB) {
        return new PlanLimitExceededException("Storage limit reached: " + usedMB + "MB/" + limitMB + "MB");
    }
}
