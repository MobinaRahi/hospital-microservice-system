package hospital.notificationservice.exception.notificationtemplate;

/**
 * Exception thrown when a duplicate template name is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateTemplateNameException extends RuntimeException {

    public DuplicateTemplateNameException(String name) {
        super("Notification template with name '" + name + "' already exists");
    }
}
