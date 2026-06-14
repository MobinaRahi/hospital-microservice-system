package hospital.coreservice.dto.department;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing department.
 * <p>
 * All fields are optional except the ID, allowing partial updates.
 * Only fields that are provided will be updated.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DepartmentUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the department to be updated.
     * This field is required to identify the department.
     */
    @NotNull(message = "Department ID is required for update")
    private Long id;

    // ========== Basic Information (Optional) ==========

    /**
     * Full name of the department.
     * <p>Example: "Cardiology", "Internal Medicine"</p>
     */
    private String departmentName;

    /**
     * Unique code identifier for the department.
     * <p>Example: "CARD", "IMED", "EMER"</p>
     */
    private String departmentCode;

    /**
     * Detailed description of the department's services.
     */
    private String description;

    /**
     * Physical location of the department.
     * <p>Example: "3rd Floor, East Wing"</p>
     */
    private String location;

    // ========== Department Leadership (Optional) ==========

    /**
     * ID of the doctor who is the head of this department.
     * Can be null to remove the current head doctor.
     */
    private Long headDoctorId;

    /**
     * ID of the nurse who is the head nurse of this department.
     * Can be null to remove the current head nurse.
     */
    private Long headNurseId;

    // ========== Contact Information (Optional) ==========

    /**
     * Department's contact phone number (11 digits).
     * <p>Example: "02122345678"</p>
     */
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String phoneNumber;

    // ========== Status (Optional) ==========

    /**
     * Active status of the department.
     * <p>- true: Active and operational</p>
     * <p>- false: Inactive (temporarily closed or merged)</p>
     */
    private Boolean isActive;
}
