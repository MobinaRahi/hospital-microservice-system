package hospital.coreservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * User roles:
 * SUPER_ADMIN, ADMIN, DOCTOR, NURSE, RECEPTIONIST, PATIENT, etc.
 *
 * @author Mobina
 */
public enum RoleName implements GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    DOCTOR,
    NURSE,
    RECEPTIONIST,
    PHARMACIST,
    LAB_TECHNICIAN,
    ACCOUNTANT,
    RADIOLOGIST,
    PATIENT;


    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
