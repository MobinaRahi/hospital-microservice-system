package hospital.notificationservice.exception.notificationtemplate;

/**
 * Exception thrown when trying to use an inactive template.
 *
 * @author MobinaRahi
 */
public class InactiveTemplateException extends RuntimeException {

    public InactiveTemplateException(String message) {
        super(message);
    }

    public static InactiveTemplateException templateIsInactive(String name) {
        return new InactiveTemplateException("Template '" + name + "' is inactive and cannot be used");
    }

    public static InactiveTemplateException templateIsInactive(Long id) {
        return new InactiveTemplateException("Template with id " + id + " is inactive and cannot be used");
    }
}
