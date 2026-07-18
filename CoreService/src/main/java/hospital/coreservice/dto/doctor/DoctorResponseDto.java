package hospital.coreservice.dto.doctor;


import hospital.coreservice.dto.department.DepartmentSlimResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
/**
 * DTO for doctor response data.
 *
 * @author Mobina
 */
public class DoctorResponseDto {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String phoneNumber;

    private Speciality speciality;

    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    private String licenseNumber;

    private Integer yearsOfExperience;

    private Long consultationFee;

    private Integer maxAppointmentsPerDay;

    private Integer defaultSlotDuration;

    private DepartmentSlimResponseDto department;

    private List<DoctorScheduleResponseDto> schedules = new ArrayList<>();

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
