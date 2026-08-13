package hospital.notificationservice.exception.notificationtemplate;

/**
 * Exception thrown when a notification template is not found.
 *
 * @author MobinaRahi
 */
public class NotificationTemplateNotFoundException extends RuntimeException {

    public NotificationTemplateNotFoundException(String message) {
        super(message);
    }

    public static NotificationTemplateNotFoundException byId(Long id) {
        return new NotificationTemplateNotFoundException("Notification template with id " + id + " not found");
    }

    public static NotificationTemplateNotFoundException byName(String name) {
        return new NotificationTemplateNotFoundException("Notification template with name '" + name + "' not found");
    }
}
