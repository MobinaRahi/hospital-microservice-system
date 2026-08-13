package hospital.adminservice.exception.systemconfig;

public class SystemConfigNotFoundException extends RuntimeException {
    public SystemConfigNotFoundException(String message) { super(message); }
    public static SystemConfigNotFoundException byId(Long id) {
        return new SystemConfigNotFoundException("SystemConfig with id " + id + " not found");
    }
    public static SystemConfigNotFoundException byKey(String key) {
        return new SystemConfigNotFoundException("SystemConfig with key '" + key + "' not found");
    }
}
