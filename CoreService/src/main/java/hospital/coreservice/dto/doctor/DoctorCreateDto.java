package hospital.coreservice.dto.doctor;

import com.hospital.coreService.model.enums.Speciality;
import com.hospital.coreService.model.enums.SubSpeciality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a new doctor.
 * <p>
 * This DTO contains only the fields that are required for creating a doctor.
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
public class DoctorCreateDto {

    // ========== Personal Information ==========

    /**
     * Doctor's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * Doctor's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * Doctor's mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    // ========== Professional Information ==========

    /**
     * Doctor's primary medical specialty (CARDIOLOGY, INTERNAL_MEDICINE, etc.).
     */
    @NotNull(message = "Specialty is required")
    private Speciality speciality;

    /**
     * List of sub-specialty IDs to be added to this doctor.
     * <p>Optional - can be empty list.</p>
     */
    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    /**
     * Medical council license number (unique identifier for certified doctors).
     */
    @NotBlank(message = "License number is required")
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
    @NotNull(message = "Consultation fee is required")
    @Positive(message = "Consultation fee must be positive")
    private Long consultationFee;

    // ========== Business Rules ==========

    /**
     * Maximum number of appointments this doctor can have per day.
     */
    @NotNull(message = "Max appointments per day is required")
    @Positive(message = "Max appointments per day must be positive")
    private Integer maxAppointmentsPerDay;

    /**
     * Default duration of each appointment slot in minutes.
     * <p>Typically 15-20 minutes for general practice, 30-45 for specialists.</p>
     */
    @NotNull(message = "Default slot duration is required")
    @Positive(message = "Default slot duration must be positive")
    private Integer defaultSlotDuration;

    // ========== Relationships (Just IDs) ==========

    /**
     * ID of the department this doctor belongs to.
     * <p>Optional - can be null if not yet assigned.</p>
     */
    private Long departmentId;
}