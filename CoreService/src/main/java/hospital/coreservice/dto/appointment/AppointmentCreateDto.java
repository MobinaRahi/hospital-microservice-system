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
 * DTO for creating a new appointment.
 * <p>
 * This DTO is used when a receptionist or a patient schedules a new appointment.
 * It contains only the fields that are required for creating an appointment.
 * Fields like createdAt, createdBy, deleted are managed automatically by the system
 * and should not be sent by the client.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class AppointmentCreateDto {

    // ========== Relationships (Just IDs, not full objects) ==========

    /**
     * ID of the patient who will attend the appointment.
     * Must reference an existing patient in the database.
     */
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    /**
     * ID of the doctor who will perform the appointment.
     * Must reference an existing doctor in the database.
     */
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    /**
     * ID of the department where the appointment will take place.
     * Must reference an existing department in the database.
     */
    @NotNull(message = "Department ID is required")
    private Long departmentId;

    // ========== Schedule Information ==========

    /**
     * Date of the appointment (e.g., 2025-05-20).
     * Must be a future date.
     */
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    /**
     * Start time of the appointment (e.g., 10:00).
     */
    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    /**
     * End time of the appointment (calculated as startTime + slotDuration).
     */
    @NotNull(message = "End time is required")
    private LocalTime endTime;

    // ========== Status & Type ==========

    /**
     * Current status of the appointment.
     * <p>
     * For new appointments, this is usually "SCHEDULED" by default.
     * If not provided, the service layer will set it to SCHEDULED.
     * </p>
     */
    private AppointmentStatus status;

    /**
     * Type of appointment (IN_PERSON, VIDEO, PHONE, EMERGENCY).
     */
    @NotNull(message = "Appointment type is required")
    private AppointmentType type;

    // ========== Additional Information (Optional) ==========

    /**
     * Reason for the visit (patient's complaint or reason for appointment).
     * Optional field.
     */
    private String reason;

    /**
     * Additional notes or remarks about the appointment.
     * Optional field.
     */
    private String notes;
}
