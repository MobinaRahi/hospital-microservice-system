package hospital.coreservice.dto.nurse;

import hospital.coreservice.model.enums.NursePosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for nurse response data.
 *
 * @author Mobina
 */
public class NurseSlimResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String nationalId;

    private String phoneNumber;

    private String email;

    private String nurseCode;

    private NursePosition position;

    private Integer yearsOfExperience;

    private Boolean isActive;
}