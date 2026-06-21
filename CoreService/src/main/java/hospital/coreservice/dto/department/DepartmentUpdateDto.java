package hospital.coreservice.dto.department;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentUpdateDto {

    @NotNull(message = "Department ID is required for update")
    private Long id;

    private String departmentName;

    private String departmentCode;

    private String description;

    private String location;

    private Long headDoctorId;

    private Long headNurseId;

    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String phoneNumber;

    private Boolean isActive;
}
