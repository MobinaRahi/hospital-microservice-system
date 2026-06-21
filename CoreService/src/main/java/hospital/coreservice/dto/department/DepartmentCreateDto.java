package hospital.coreservice.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DepartmentCreateDto {

    @NotBlank(message = "Department name is required")
    private String departmentName;


    @NotBlank(message = "Department code is required")
    private String departmentCode;

    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private Long headDoctorId;

    private Long headNurseId;

    private List<Long> doctorIds = new ArrayList<>();

    private List<Long> nurseIds = new ArrayList<>();

    private List<Long> roomIds = new ArrayList<>();

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String phoneNumber;
}
