package hospital.coreservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

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
        return this.name();
    }
}
