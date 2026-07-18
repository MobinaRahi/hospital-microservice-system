package hospital.coreservice.dto.request;

import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
/**
 * Request DTO for request operations.
 *
 * @author Mobina
 */
public class CompleteRegistrationRequest {
    @NotNull
    private Long patientId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private BloodType bloodType;
    private String address;
    private String allergies;
    private String newPassword;
    private String confirmPassword;
}