package hospital.coreservice.dto.appointment;

import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentResponseDto {

    private Long id;

    private PatientResponseDto patient;

    private DoctorResponseDto doctor;

    private DepartmentResponseDto department;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentStatus status;

    private AppointmentType type;

    private String reason;

    private String notes;

    private Long canceledBy;

    private String canceledReason;

    private LocalDateTime canceledAt;

    private LocalDateTime createdAt;

    private Long createdBy;
}
