package hospital.notificationservice.exception.inappnotification;

/**
 * Exception thrown when an in-app notification is not found.
 *
 * @author MobinaRahi
 */
public class InAppNotificationNotFoundException extends RuntimeException {

    public InAppNotificationNotFoundException(String message) {
        super(message);
    }

    public static InAppNotificationNotFoundException byId(Long id) {
        return new InAppNotificationNotFoundException("In-app notification with id " + id + " not found");
    }

    public static InAppNotificationNotFoundException byUserId(Long userId) {
        return new InAppNotificationNotFoundException("In-app notifications for user id " + userId + " not found");
    }
}
