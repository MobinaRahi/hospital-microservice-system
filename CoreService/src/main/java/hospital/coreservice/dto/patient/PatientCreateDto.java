package hospital.coreservice.dto.patient;

import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO for creating a new patient.
 * <p>
 * This DTO contains only the fields that are required for creating a patient.
 * Fields like createdAt, updatedAt, createdBy, deleted are managed automatically.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class PatientCreateDto {

    // ========== Personal Information ==========

    /**
     * National ID (10 digits, unique).
     * <p>Example: "0123456789"</p>
     */
    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    /**
     * Patient's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * Patient's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * Patient's gender.
     */
    @NotNull(message = "Gender is required")
    private Gender gender;

    // ========== Contact Information ==========

    /**
     * Patient's mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    /**
     * Patient's full residential address.
     */
    @NotBlank(message = "Address is required")
    private String address;

    // ========== Medical Information ==========

    /**
     * Patient's blood type.
     */
    @NotNull(message = "Blood type is required")
    private BloodType bloodType;

    /**
     * Insurance ID reference (links to insurance service).
     * <p>Optional - can be null if patient has no insurance.</p>
     */
    private Long insuranceId;

    /**
     * Known allergies (medication, food, environmental, etc.).
     * <p>Optional field.</p>
     */
    private String allergies;

    /**
     * Patient's date of birth.
     * <p>Example: 1990-05-15</p>
     */
    private LocalDate birthDate;
}