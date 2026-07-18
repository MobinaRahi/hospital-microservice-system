package hospital.coreservice.dto.nurse;

import hospital.coreservice.model.enums.NursePosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * DTO for updating an existing nurse.
 *
 * @author Mobina
 */
public class NurseUpdateDto {

    @NotNull(message = "Nurse ID is required for update")
    private Long id;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private String nurseCode;

    private List<Long> departmentIds = new ArrayList<>();

    private NursePosition position;

    private List<Long> shiftPreferenceIds = new ArrayList<>();

    private Boolean isActive;
}
