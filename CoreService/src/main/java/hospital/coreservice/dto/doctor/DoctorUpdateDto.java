package hospital.coreservice.dto.doctor;


import hospital.coreservice.model.enums.Speciality;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
/**
 * DTO for updating an existing doctor.
 *
 * @author Mobina
 */
public class DoctorUpdateDto {

    @NotNull(message = "Doctor ID is required for update")
    private Long id;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    private Speciality speciality;

    private String licenseNumber;

    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;

    @Positive(message = "Consultation fee must be positive")
    private BigDecimal consultationFee;

    @Positive(message = "Max appointments per day must be positive")
    private Integer maxAppointmentsPerDay;

    @Positive(message = "Default slot duration must be positive")
    private Integer defaultSlotDuration;

    private Long departmentId;

    private Boolean isActive;
}
