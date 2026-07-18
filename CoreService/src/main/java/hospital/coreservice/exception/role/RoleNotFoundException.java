package hospital.coreservice.exception.role;

import hospital.coreservice.model.enums.RoleName;

/**
 * Thrown when a role is not found.
 *
 * @author Mobina
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role not found with id: " + id);
    }

    public RoleNotFoundException(RoleName name) {
        super("Role not found with name: " + name);
    }
}