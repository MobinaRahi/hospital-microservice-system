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

/**
 * DTO for sending appointment data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * Unlike the Entity, this DTO contains only the information that should
 * be exposed to the client, with relationships represented as nested DTOs.
 * Sensitive fields like 'deleted' are excluded.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class AppointmentResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the appointment.
     */
    private Long id;

    // ========== Relationships (Full objects for display) ==========

    /**
     * Patient associated with this appointment.
     * Contains full patient information (id, name, nationalId, etc.).
     */
    private PatientResponseDto patient;

    /**
     * Doctor who will see the patient.
     * Contains full doctor information (id, name, specialty, etc.).
     */
    private DoctorResponseDto doctor;

    /**
     * Department where the appointment takes place.
     * Contains full department information (id, name, location, etc.).
     */
    private DepartmentResponseDto department;

    // ========== Schedule Information ==========

    /**
     * Date of the appointment (e.g., 2025-05-20).
     */
    private LocalDate appointmentDate;

    /**
     * Start time of the appointment (e.g., 10:00).
     */
    private LocalTime startTime;

    /**
     * End time of the appointment (calculated as startTime + slotDuration).
     */
    private LocalTime endTime;

    // ========== Status & Type ==========

    /**
     * Current status of the appointment.
     * <p>Possible values: SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW</p>
     */
    private AppointmentStatus status;

    /**
     * Type of appointment.
     * <p>Possible values: IN_PERSON, VIDEO, PHONE, EMERGENCY</p>
     */
    private AppointmentType type;

    // ========== Additional Information ==========

    /**
     * Reason for the visit (patient's complaint or reason for appointment).
     */
    private String reason;

    /**
     * Additional notes or remarks about the appointment.
     */
    private String notes;

    // ========== Cancellation Information ==========

    /**
     * ID of the user who cancelled this appointment.
     * <p>Null if the appointment is not cancelled.</p>
     */
    private Long canceledBy;

    /**
     * Reason provided for cancellation.
     * <p>Null if the appointment is not cancelled.</p>
     */
    private String canceledReason;

    /**
     * Timestamp when the cancellation occurred.
     * <p>Null if the appointment is not cancelled.</p>
     */
    private LocalDateTime canceledAt;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this appointment was created.
     */
    private LocalDateTime createdAt;

    /**
     * ID of the user who created this appointment (receptionist or patient).
     */
    private Long createdBy;
}