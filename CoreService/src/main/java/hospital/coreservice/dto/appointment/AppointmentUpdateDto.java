package hospital.coreservice.dto.appointment;

import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for updating an existing appointment.
 *
 * @author Mobina
 */
public class AppointmentUpdateDto {

    @NotNull(message = "Appointment ID is required for update")
    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long departmentId;

    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentStatus status;

    private AppointmentType type;

    private String reason;

    private String notes;
}