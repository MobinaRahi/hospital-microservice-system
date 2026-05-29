package hospital.coreservice.dto.nurse;

import com.hospital.coreService.model.enums.NursePosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a new nurse.
 * <p>
 * This DTO contains only the fields that are required for creating a nurse.
 * Fields like userId, createdAt, updatedAt are managed automatically by the system.
 * Relationships are represented by IDs, not full objects.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class NurseCreateDto {

    // ========== Personal Information ==========

    /**
     * Nurse's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * Nurse's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * National ID (10 digits, unique).
     * <p>Stored as String to preserve leading zeros (e.g., "0123456789")</p>
     */
    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    // ========== Contact Information ==========

    /**
     * Mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    /**
     * Email address for professional communication.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // ========== Professional Information ==========

    /**
     * Unique employee code for the nurse.
     * <p>Format: "NUR-XXXXX" where XXXXX is a sequential number.</p>
     */
    @NotBlank(message = "Nurse code is required")
    private String nurseCode;

    /**
     * List of department IDs where this nurse works.
     * <p>A nurse can be assigned to multiple departments.</p>
     * <p>Optional - can be empty list.</p>
     */
    private List<Long> departmentIds = new ArrayList<>();

    /**
     * Nurse's position/rank (HEAD_NURSE, SENIOR_NURSE, STAFF_NURSE, etc.).
     */
    @NotNull(message = "Nurse position is required")
    private NursePosition position;

    /**
     * List of shift IDs that this nurse prefers.
     * <p>Optional - can be empty list.</p>
     */
    private List<Long> shiftPreferenceIds = new ArrayList<>();
}