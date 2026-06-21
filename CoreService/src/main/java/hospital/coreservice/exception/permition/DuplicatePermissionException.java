package hospital.coreservice.exception.permition;

public class DuplicatePermissionException extends RuntimeException {
    public DuplicatePermissionException(String name) {
        super("Permission already exists with name: " + name);
    }
}