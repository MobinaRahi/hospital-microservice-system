package hospital.coreservice.exception.role;

/**
 * Exception for role operations.
 *
 * @author Mobina
 */
public class RoleInUseException extends RuntimeException {
    public RoleInUseException(Long roleId, String roleName, long userCount) {
        super("Role '" + roleName + "' (id: " + roleId + ") is assigned to " + userCount + " users and cannot be deleted.");
    }
}