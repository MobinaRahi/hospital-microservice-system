package hospital.coreservice.dto.nurse;

import hospital.coreservice.model.enums.NursePosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for updating an existing nurse.
 * <p>
 * All fields are optional except the ID, allowing partial updates.
 * Only fields that are provided will be updated.
 * Example: Update only the phone number without affecting other fields.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class NurseUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the nurse to be updated.
     * This field is required to identify the nurse.
     */
    @NotNull(message = "Nurse ID is required for update")
    private Long id;

    // ========== Personal Information (Optional) ==========

    /**
     * Nurse's first name.
     */
    private String firstName;

    /**
     * Nurse's last name.
     */
    private String lastName;

    /**
     * National ID (10 digits, unique).
     * <p>Stored as String to preserve leading zeros (e.g., "0123456789")</p>
     * <p>Optional for partial updates.</p>
     */
    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    // ========== Contact Information (Optional) ==========

    /**
     * Mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     * <p>Optional for partial updates.</p>
     */
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    /**
     * Email address for professional communication.
     * <p>Optional for partial updates.</p>
     */
    @Email(message = "Invalid email format")
    private String email;

    // ========== Professional Information (Optional) ==========

    /**
     * Unique employee code for the nurse.
     * <p>Format: "NUR-XXXXX" where XXXXX is a sequential number.</p>
     * <p>Optional for partial updates.</p>
     */
    private String nurseCode;

    /**
     * List of department IDs where this nurse works.
     * <p>A nurse can be assigned to multiple departments.</p>
     * <p>Optional - can be empty list to remove all department assignments.</p>
     */
    private List<Long> departmentIds = new ArrayList<>();

    /**
     * Nurse's position/rank (HEAD_NURSE, SENIOR_NURSE, STAFF_NURSE, etc.).
     * <p>Optional for partial updates.</p>
     */
    private NursePosition position;

    /**
     * List of shift IDs that this nurse prefers.
     * <p>Optional - can be empty list to remove all shift preferences.</p>
     */
    private List<Long> shiftPreferenceIds = new ArrayList<>();

    // ========== Status (Optional) ==========

    /**
     * Active/Inactive status of the nurse.
     * <p>- true: Currently employed and active in the system</p>
     * <p>- false: No longer employed or on extended leave</p>
     * <p>Optional for partial updates.</p>
     */
    private Boolean isActive;
}
