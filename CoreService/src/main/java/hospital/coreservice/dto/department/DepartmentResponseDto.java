package hospital.coreservice.dto.department;

import hospital.coreservice.dto.doctor.DoctorSlimResponseDto;
import hospital.coreservice.dto.nurse.NurseSlimResponseDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * DTO for department response data.
 *
 * @author Mobina
 */
public class DepartmentResponseDto {

    private Long id;

    private String departmentName;

    private String departmentCode;

    private String description;

    private String location;

    private DoctorSlimResponseDto headDoctor;

    private NurseSlimResponseDto headNurse;

    private List<DoctorSlimResponseDto> doctors = new ArrayList<>();

    private List<NurseSlimResponseDto> nurses = new ArrayList<>();

    private List<RoomResponseDto> rooms = new ArrayList<>();

    private String phoneNumber;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
