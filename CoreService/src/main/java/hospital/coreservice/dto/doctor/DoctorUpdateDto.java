package hospital.coreservice.dto.doctor;


import hospital.coreservice.model.enums.Speciality;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for updating an existing doctor.
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
public class DoctorUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the doctor to be updated.
     * This field is required to identify the doctor.
     */
    @NotNull(message = "Doctor ID is required for update")
    private Long id;

    // ========== Personal Information (Optional) ==========

    /**
     * Doctor's first name.
     */
    private String firstName;

    /**
     * Doctor's last name.
     */
    private String lastName;

    /**
     * Doctor's mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    // ========== Professional Information (Optional) ==========

    /**
     * Doctor's primary medical specialty (CARDIOLOGY, INTERNAL_MEDICINE, etc.).
     */
    private Speciality speciality;

    /**
     * Medical council license number (unique identifier for certified doctors).
     */
    private String licenseNumber;

    /**
     * Number of years of professional experience.
     */
    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;

    /**
     * Consultation fee in Rials (Iranian currency).
     * <p>Example: 500000 means 500,000 Rials</p>
     */
    @Positive(message = "Consultation fee must be positive")
    private BigDecimal consultationFee;

    // ========== Business Rules (Optional) ==========

    /**
     * Maximum number of appointments this doctor can have per day.
     */
    @Positive(message = "Max appointments per day must be positive")
    private Integer maxAppointmentsPerDay;

    /**
     * Default duration of each appointment slot in minutes.
     * <p>Typically 15-20 minutes for general practice, 30-45 for specialists.</p>
     */
    @Positive(message = "Default slot duration must be positive")
    private Integer defaultSlotDuration;

    // ========== Relationships (Optional) ==========

    /**
     * ID of the department this doctor belongs to.
     * <p>Can be null to remove the doctor from their current department.</p>
     */
    private Long departmentId;

    // ========== Status (Optional) ==========

    /**
     * Active/Inactive status of the doctor.
     * <p>- true: Active and practicing</p>
     * <p>- false: Inactive (on leave, retired)</p>
     */
    private Boolean isActive;
}
