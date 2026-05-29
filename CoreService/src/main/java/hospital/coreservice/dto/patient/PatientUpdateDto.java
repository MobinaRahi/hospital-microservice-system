package hospital.coreservice.dto.patient;

import com.hospital.coreService.model.enums.BloodType;
import com.hospital.coreService.model.enums.Gender;
import com.hospital.coreService.model.enums.PatientStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO for updating an existing patient.
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
public class PatientUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the patient to be updated.
     */
    @NotNull(message = "Patient ID is required for update")
    private Long id;

    // ========== Personal Information (Optional) ==========

    /**
     * National ID (10 digits, unique).
     */
    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
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

    // ========== Contact Information (Optional) ==========

    /**
     * Patient's mobile phone number (11 digits).
     */
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    /**
     * Patient's full residential address.
     */
    private String address;

    // ========== Medical Information (Optional) ==========

    /**
     * Patient's blood type.
     */
    private BloodType bloodType;

    /**
     * Insurance ID reference.
     */
    private Long insuranceId;

    /**
     * Known allergies.
     */
    private String allergies;

    // ========== Status (Optional) ==========

    /**
     * Current patient status (ACTIVE, ARCHIVED, DECEASED, TRANSFERRED).
     */
    private PatientStatus status;

    /**
     * Patient's date of birth.
     * <p>Example: 1990-05-15</p>
     */
    private LocalDate birthDate;
}