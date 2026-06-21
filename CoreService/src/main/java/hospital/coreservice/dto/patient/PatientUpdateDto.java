package hospital.coreservice.dto.patient;

import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientUpdateDto {

    @NotNull(message = "Patient ID is required for update")
    private Long id;

    @Pattern(regexp = "^[0-9]{10}$", message = "National ID must be 10 digits")
    private String nationalId;

    private String firstName;

    private String lastName;

    private Gender gender;

    @Pattern(regexp = "^09[0-9]{9}$", message = "Phone number must be a valid Iranian mobile number")
    private String phoneNumber;

    private String address;

    private BloodType bloodType;

    private Long insuranceId;

    private String allergies;

    private PatientStatus status;

    private LocalDate birthDate;
}