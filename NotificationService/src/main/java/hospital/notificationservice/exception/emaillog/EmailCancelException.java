package hospital.notificationservice.exception.emaillog;

/**
 * Exception thrown when an email cannot be cancelled due to its current status.
 *
 * @author MobinaRahi
 */
public class EmailCancelException extends RuntimeException {

    public EmailCancelException(String message) {
        super(message);
    }

    public static EmailCancelException cannotCancel(String currentStatus) {
        return new EmailCancelException("Cannot cancel email in status: " + currentStatus + ". Only PENDING emails can be cancelled.");
    }
}
