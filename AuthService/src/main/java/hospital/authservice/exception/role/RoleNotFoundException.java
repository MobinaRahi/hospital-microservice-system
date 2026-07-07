package hospital.authservice.exception.role;

import hospital.authservice.model.enums.RoleName;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role not found with id: " + id);
    }

    public RoleNotFoundException(RoleName name) {
        super("Role not found with name: " + name);
    }
}