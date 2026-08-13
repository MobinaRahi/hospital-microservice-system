package hospital.notificationservice.exception.smsgateway;

/**
 * Exception thrown when an SMS cannot be cancelled due to its current status.
 *
 * @author MobinaRahi
 */
public class SmsCancelException extends RuntimeException {

    public SmsCancelException(String message) {
        super(message);
    }

    public static SmsCancelException cannotCancel(String currentStatus) {
        return new SmsCancelException("Cannot cancel SMS in status: " + currentStatus + ". Only PENDING SMS can be cancelled.");
    }
}
