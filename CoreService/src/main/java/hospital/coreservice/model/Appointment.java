package hospital.coreservice.model;

import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "appointmentEntity")
@Table(name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_patient", columnList = "patient_id"),
                @Index(name = "idx_appointment_doctor", columnList = "doctor_id"),
                @Index(name = "idx_appointment_date", columnList = "appointment_date"),
                @Index(name = "idx_appointment_status", columnList = "status")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
/**
 * Represents a scheduled appointment.
 * Links patient, doctor, department, date, and time.
 * Has status workflow: SCHEDULED → CHECK_IN → IN_PROGRESS → COMPLETED/CANCELLED
 *
 * @author Mobina
 */
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AppointmentStatus status=AppointmentStatus.SCHEDULED;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "canceled_by")
    private Long canceledBy;

    @Column(name = "canceled_reason", length = 100)
    private String canceledReason;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    /**
     * User ID who checked in the patient.
     */
    @Column(name = "checked_in_by")
    private Long checkedInBy;

    /**
     * Timestamp when patient checked in.
     */
    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if the appointment is in the future.
     *
     * @return true if appointment date is after today
     */
    public boolean isUpcoming() {
        return this.appointmentDate.isAfter(LocalDate.now());
    }

    /**
     * Checks if the appointment is today.
     *
     * @return true if appointment date is today
     */
    public boolean isToday() {
        return this.appointmentDate.equals(LocalDate.now());
    }

    /**
     * Checks in the patient.
     *
     * @param userId the receptionist user ID
     */
    public void checkIn(Long userId) {
        if (this.status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Can only check in SCHEDULED appointments. Current status: " + this.status);
        }
        this.status = AppointmentStatus.CHECK_IN;
        this.checkedInBy = userId;
        this.checkedInAt = LocalDateTime.now();
    }

    /**
     * Marks the appointment as in progress (patient with doctor).
     */
    public void startVisit() {
        if (this.status != AppointmentStatus.CHECK_IN) {
            throw new IllegalStateException("Can only start visit for CHECK_IN appointments. Current status: " + this.status);
        }
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    /**
     * Marks the appointment as completed.
     */
    public void complete() {
        if (this.status != AppointmentStatus.IN_PROGRESS && this.status != AppointmentStatus.CHECK_IN) {
            throw new IllegalStateException("Cannot complete appointment in status: " + this.status);
        }
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Cancels the appointment.
     */
    public void cancel() {
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a COMPLETED appointment");
        }
        this.status = AppointmentStatus.CANCELLED;
    }

    /**
     * Marks the patient as no-show.
     */
    public void markNoShow() {
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot mark no-show for COMPLETED appointment");
        }
        this.status = AppointmentStatus.NO_SHOW;
    }

    /**
     * Checks if the appointment is checked in.
     *
     * @return true if status is CHECK_IN
     */
    public boolean isCheckedIn() {
        return this.status == AppointmentStatus.CHECK_IN;
    }

    /**
     * Checks if the appointment is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return this.status == AppointmentStatus.COMPLETED;
    }
}

