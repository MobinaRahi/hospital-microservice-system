package hospital.coreservice.dto.nurse;

import com.hospital.coreService.dto.department.DepartmentResponseDto;
import com.hospital.coreService.dto.shift.ShiftResponseDto;
import com.hospital.coreService.model.enums.NursePosition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for sending nurse data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * Unlike the Entity, this DTO contains only the information that should
 * be exposed to the client, with relationships represented as nested DTOs.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class NurseResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the nurse.
     */
    private Long id;

    /**
     * User ID reference from Auth Service.
     * <p>Links this nurse to a system user account for login and permissions.</p>
     */
    private Long userId;

    // ========== Personal Information ==========

    /**
     * Nurse's first name.
     */
    private String firstName;

    /**
     * Nurse's last name.
     */
    private String lastName;

    /**
     * Nurse's full name (firstName + lastName).
     * <p>Example: "مریم کریمی"</p>
     */
    private String fullName;

    /**
     * National ID (10 digits).
     * <p>Example: "0123456789"</p>
     */
    private String nationalId;

    // ========== Contact Information ==========

    /**
     * Mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    private String phoneNumber;

    /**
     * Email address for professional communication.
     */
    private String email;

    // ========== Professional Information ==========

    /**
     * Unique employee code for the nurse.
     * <p>Format: "NUR-XXXXX"</p>
     */
    private String nurseCode;

    /**
     * List of departments where this nurse works.
     * <p>Each department contains full information (id, name, location, etc.).</p>
     * <p>Empty list if not assigned to any department.</p>
     */
    private List<DepartmentResponseDto> departments = new ArrayList<>();

    /**
     * Nurse's position/rank (HEAD_NURSE, SENIOR_NURSE, STAFF_NURSE, etc.).
     */
    private NursePosition position;

    /**
     * List of shift preferences for this nurse.
     * <p>Each shift contains full information (id, name, startTime, endTime, etc.).</p>
     * <p>Empty list if no shift preferences are set.</p>
     */
    private List<ShiftResponseDto> shiftPreferences = new ArrayList<>();

    /**
     * Number of years of professional experience.
     */
    private Integer yearsOfExperience;

    // ========== Status ==========

    /**
     * Active/Inactive status of the nurse.
     * <p>- true: Currently employed and active in the system</p>
     * <p>- false: No longer employed or on extended leave</p>
     */
    private Boolean isActive;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this nurse was created.
     */
    private LocalDateTime createdAt;
}