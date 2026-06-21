package hospital.coreservice.dto.doctor;

import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DoctorSlimResponseDto {
    private Long id;

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

    private Boolean isActive;
}