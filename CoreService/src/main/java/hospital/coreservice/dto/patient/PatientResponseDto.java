package hospital.coreservice.dto.patient;

import com.hospital.coreService.dto.room.RoomResponseDto;
import com.hospital.coreService.model.enums.BloodType;
import com.hospital.coreService.model.enums.Gender;
import com.hospital.coreService.model.enums.PatientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for sending patient data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class PatientResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the patient.
     */
    private Long id;

    // ========== Personal Information ==========

    /**
     * Patient's full name (firstName + lastName).
     */
    private String fullName;

    /**
     * National ID (10 digits).
     */
    private String nationalId;

    /**
     * Patient's first name.
     */
    private String firstName;

    /**
     * Patient's last name.
     */
    private String lastName;

    /**
     * Patient's gender.
     */
    private Gender gender;

    // ========== Contact Information ==========

    /**
     * Mobile phone number.
     */
    private String phoneNumber;

    /**
     * Full residential address.
     */
    private String address;

    // ========== Medical Information ==========

    /**
     * Blood type.
     */
    private BloodType bloodType;

    /**
     * Insurance ID reference.
     */
    private Long insuranceId;

    /**
     * Current patient status.
     */
    private PatientStatus status;

    /**
     * Known allergies.
     */
    private String allergies;

    // ========== Relationships ==========

    /**
     * Current room/bed where this patient is assigned.
     * <p>Null if not admitted.</p>
     */
    private RoomResponseDto currentRoom;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this patient was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when this patient was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * ID of the user who created this patient record.
     */
    private Long createdBy;

    /**
     * Patient's date of birth.
     * <p>Example: 1990-05-15</p>
     */
    private LocalDate birthDate;
}