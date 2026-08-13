package hospital.notificationservice.exception.inappnotification;

/**
 * Exception thrown when trying to mark an already-read notification as read again.
 *
 * @author MobinaRahi
 */
public class AlreadyReadNotificationException extends RuntimeException {

    public AlreadyReadNotificationException(String message) {
        super(message);
    }

    public static AlreadyReadNotificationException byId(Long id) {
        return new AlreadyReadNotificationException("Notification with id " + id + " is already marked as read");
    }
}
