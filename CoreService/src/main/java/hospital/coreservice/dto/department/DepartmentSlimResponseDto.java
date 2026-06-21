package hospital.coreservice.dto.department;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentSlimResponseDto {
    private Long id;

    private String departmentName;

    private String departmentCode;

    private String description;

    private String location;

    private String phoneNumber;

    private Boolean isActive;
}