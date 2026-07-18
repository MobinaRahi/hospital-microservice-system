package hospital.coreservice.dto.nurse;

import hospital.coreservice.dto.department.DepartmentSlimResponseDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.model.enums.NursePosition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * DTO for nurse response data.
 *
 * @author Mobina
 */
public class NurseResponseDto {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String nationalId;

    private String phoneNumber;

    private String email;

    private String nurseCode;

    private List<DepartmentSlimResponseDto> departments = new ArrayList<>();

    private NursePosition position;

    private List<ShiftResponseDto> shiftPreferences = new ArrayList<>();

    private Integer yearsOfExperience;

    private Boolean isActive;

    private LocalDateTime createdAt;
}
