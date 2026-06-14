package hospital.coreservice.model;

import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing a patient's appointment with a doctor.
 * <p>
 * This class represents a scheduled appointment where a patient visits a doctor
 * at a specific date and time. Each appointment belongs to one patient,
 * one doctor, and one department. It tracks the entire lifecycle from
 * scheduling to completion or cancellation.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "appointmentEntity")
@Table(name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_patient", columnList = "patient_id"),
                @Index(name = "idx_appointment_doctor", columnList = "doctor_id"),
                @Index(name = "idx_appointment_date", columnList = "appointment_date"),
                @Index(name = "idx_appointment_status", columnList = "status"),
                @Index(name = "idx_appointment_deleted", columnList = "deleted")
        })
@SQLRestriction("deleted = false")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Appointment {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Relationships ==========
    /**
     * Patient associated with this appointment.
     * <p>Many appointments can belong to one patient.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * Doctor who will see the patient.
     * <p>Many appointments can belong to one doctor.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * Department where the appointment takes place.
     * <p>Many appointments can belong to one department.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // ========== Schedule Information ==========
    /**
     * Date of the appointment (e.g., 2025-05-20).
     */
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    /**
     * Start time of the appointment (e.g., 10:00).
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * End time of the appointment (calculated as startTime + slotDuration).
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // ========== Status & Type ==========
    /**
     * Current status of the appointment (SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    /**
     * Type of appointment (IN_PERSON, VIDEO, PHONE, EMERGENCY).
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    // ========== Additional Information ==========
    /**
     * Reason for the visit (patient's complaint or reason for appointment).
     */
    @Column(name = "reason", length = 255)
    private String reason;

    /**
     * Additional notes or remarks about the appointment.
     */
    @Column(name = "notes", length = 255)
    private String notes;

    // ========== Cancellation Information ==========
    /**
     * ID of the user who cancelled this appointment (null if not cancelled).
     */
    @Column(name = "canceled_by")
    private Long canceledBy;

    /**
     * Reason provided for cancellation.
     */
    @Column(name = "canceled_reason", length = 100)
    private String canceledReason;

    /**
     * Timestamp when the cancellation occurred (null if not cancelled).
     */
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    // ========== Audit Fields ==========
    /**
     * Timestamp when this appointment record was created.
     * <p>Should be set manually in service layer before persistence.</p>
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * ID of the user who created this appointment (receptionist or patient).
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    // ========== Soft Delete ==========
    /**
     * Soft delete flag for the appointment.
     * <p>
     * - false: Active appointment (visible in queries)
     * - true: Deleted appointment (hidden from normal queries via @SQLRestriction)
     * </p>
     */
    @Column(name = "deleted")
    private boolean deleted;

}

