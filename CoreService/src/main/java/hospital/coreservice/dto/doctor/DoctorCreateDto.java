package hospital.coreservice.dto.doctor;

import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DoctorCreateDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    @NotNull(message = "Specialty is required")
    private Speciality speciality;

    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;

    @NotNull(message = "Consultation fee is required")
    @Positive(message = "Consultation fee must be positive")
    private Long consultationFee;

    @NotNull(message = "Max appointments per day is required")
    @Positive(message = "Max appointments per day must be positive")
    private Integer maxAppointmentsPerDay;

    @NotNull(message = "Default slot duration is required")
    @Positive(message = "Default slot duration must be positive")
    private Integer defaultSlotDuration;

    private Long departmentId;
}
