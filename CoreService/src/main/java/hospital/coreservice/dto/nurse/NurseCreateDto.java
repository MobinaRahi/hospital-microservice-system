package hospital.coreservice.dto.nurse;

import hospital.coreservice.model.enums.NursePosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NurseCreateDto {

    @NotBlank(message = "userId is required")
    private Long userId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Nurse code is required")
    private String nurseCode;

    private Integer yearsOfExperience;

    private List<Long> departmentIds = new ArrayList<>();

    @NotNull(message = "Nurse position is required")
    private NursePosition position;

    private List<Long> shiftPreferenceIds = new ArrayList<>();
}
