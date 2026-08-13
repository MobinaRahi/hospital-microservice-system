package hospital.adminservice.exception.systemconfig;

public class ConfigNotEditableException extends RuntimeException {
    public ConfigNotEditableException(String key) {
        super("SystemConfig with key '" + key + "' is not editable");
    }
}
