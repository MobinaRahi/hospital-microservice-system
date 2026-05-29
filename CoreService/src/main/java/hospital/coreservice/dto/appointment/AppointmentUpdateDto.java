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
 * <p>
 * This DTO is used when modifying an appointment that has already been created.
 * Unlike CreateDto, this includes the appointment ID to identify which appointment
 * to update. All fields are optional except the ID, as partial updates are allowed.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class AppointmentUpdateDto {

    // ========== Primary Key (Required for Update) ==========

    /**
     * ID of the appointment to be updated.
     * This field is required to identify the existing appointment.
     */
    @NotNull(message = "Appointment ID is required for update")
    private Long id;

    // ========== Relationships (Just IDs, not full objects) ==========

    /**
     * ID of the patient who will attend the appointment.
     * Must reference an existing patient in the database.
     * <p>Optional for partial updates.</p>
     */
    private Long patientId;

    /**
     * ID of the doctor who will perform the appointment.
     * Must reference an existing doctor in the database.
     * <p>Optional for partial updates.</p>
     */
    private Long doctorId;

    /**
     * ID of the department where the appointment will take place.
     * Must reference an existing department in the database.
     * <p>Optional for partial updates.</p>
     */
    private Long departmentId;

    // ========== Schedule Information ==========

    /**
     * Date of the appointment (e.g., 2025-05-20).
     * Must be a future date if provided.
     * <p>Optional for partial updates.</p>
     */
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    /**
     * Start time of the appointment (e.g., 10:00).
     * <p>Optional for partial updates.</p>
     */
    private LocalTime startTime;

    /**
     * End time of the appointment (calculated as startTime + slotDuration).
     * <p>Optional for partial updates.</p>
     */
    private LocalTime endTime;

    // ========== Status & Type ==========

    /**
     * Current status of the appointment.
     * <p>Possible values: SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW</p>
     * <p>Optional for partial updates.</p>
     */
    private AppointmentStatus status;

    /**
     * Type of appointment (IN_PERSON, VIDEO, PHONE, EMERGENCY).
     * <p>Optional for partial updates.</p>
     */
    private AppointmentType type;

    // ========== Additional Information (Optional) ==========

    /**
     * Reason for the visit (patient's complaint or reason for appointment).
     * <p>Optional for partial updates.</p>
     */
    private String reason;

    /**
     * Additional notes or remarks about the appointment.
     * <p>Optional for partial updates.</p>
     */
    private String notes;
}