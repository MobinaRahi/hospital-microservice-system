package hospital.notificationservice.exception.emaillog;

/**
 * Exception thrown when an email log record is not found.
 *
 * @author MobinaRahi
 */
public class EmailLogNotFoundException extends RuntimeException {

    public EmailLogNotFoundException(String message) {
        super(message);
    }

    public static EmailLogNotFoundException byId(Long id) {
        return new EmailLogNotFoundException("Email log with id " + id + " not found");
    }

    public static EmailLogNotFoundException byRecipient(String recipient) {
        return new EmailLogNotFoundException("Email log for recipient '" + recipient + "' not found");
    }
}
