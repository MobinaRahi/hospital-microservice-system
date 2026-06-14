package hospital.coreservice.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a new department.
 * <p>
 * This DTO contains only the fields that are required for creating a department.
 * Relationships are represented by IDs, not full objects.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DepartmentCreateDto {

    // ========== Basic Information ==========

    /**
     * Full name of the department.
     * <p>Example: "Cardiology", "Internal Medicine"</p>
     */
    @NotBlank(message = "Department name is required")
    private String departmentName;

    /**
     * Unique code identifier for the department.
     * <p>Example: "CARD", "IMED", "EMER"</p>
     */
    @NotBlank(message = "Department code is required")
    private String departmentCode;

    /**
     * Detailed description of the department's services.
     * Optional field.
     */
    private String description;

    /**
     * Physical location of the department.
     * <p>Example: "3rd Floor, East Wing"</p>
     */
    @NotBlank(message = "Location is required")
    private String location;

    // ========== Department Leadership (Just IDs) ==========

    /**
     * ID of the doctor who is the head of this department.
     * Optional - can be null if not assigned yet.
     */
    private Long headDoctorId;

    /**
     * ID of the nurse who is the head nurse of this department.
     * Optional - can be null if not assigned yet.
     */
    private Long headNurseId;

    // ========== Relationships (Just IDs, not full objects) ==========

    /**
     * List of doctor IDs to be assigned to this department.
     * Optional - can be empty list.
     */
    private List<Long> doctorIds = new ArrayList<>();

    /**
     * List of nurse IDs to be assigned to this department.
     * Optional - can be empty list.
     */
    private List<Long> nurseIds = new ArrayList<>();

    /**
     * List of room IDs belonging to this department.
     * Optional - can be empty list.
     */
    private List<Long> roomIds = new ArrayList<>();

    // ========== Contact Information ==========

    /**
     * Department's contact phone number (11 digits).
     * <p>Example: "02122345678"</p>
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String phoneNumber;
}
