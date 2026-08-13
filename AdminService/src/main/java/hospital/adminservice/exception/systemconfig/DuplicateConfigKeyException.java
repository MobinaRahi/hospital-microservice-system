package hospital.adminservice.exception.systemconfig;

public class DuplicateConfigKeyException extends RuntimeException {
    public DuplicateConfigKeyException(String key) {
        super("SystemConfig with key '" + key + "' already exists");
    }
}
