package hospital.coreservice.model;

import hospital.coreservice.model.enums.Priority;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a patient's entry in the waiting queue for today.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Queue entries are created when patient checks in</li>
 *   <li>Priority determines queue order (EMERGENCY > URGENT > NORMAL)</li>
 *   <li>Queue is reset daily</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "core_queue_entries",
        indexes = {
                @Index(name = "idx_queue_doctor", columnList = "doctorId"),
                @Index(name = "idx_queue_status", columnList = "status"),
                @Index(name = "idx_queue_date", columnList = "queueDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QueueEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Appointment ID this queue entry is linked to.
     */
    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    /**
     * Patient ID.
     */
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    /**
     * Doctor ID.
     */
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    /**
     * Queue date (usually today).
     */
    @Column(name = "queue_date", nullable = false)
    private LocalDate queueDate;

    /**
     * Queue position (1 = first in line).
     */
    @Column(name = "queue_position", nullable = false)
    private Integer queuePosition;

    /**
     * Priority level (determines order).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Priority priority = Priority.NORMAL;

    /**
     * Status of the queue entry.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String status;

    /**
     * Estimated wait time in minutes.
     */
    @Column(name = "estimated_wait_minutes")
    private Integer estimatedWaitMinutes;

    /**
     * Timestamp when the patient was called.
     */
    @Column(name = "called_at")
    private LocalDateTime calledAt;

    // ═══════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Marks this queue entry as called (patient going to doctor).
     */
    public void markAsCalled() {
        this.calledAt = LocalDateTime.now();
        this.status = "IN_PROGRESS";
    }

    /**
     * Marks this queue entry as completed.
     */
    public void markAsCompleted() {
        this.status = "COMPLETED";
    }

    /**
     * Checks if this queue entry is for today.
     *
     * @return true if queue date is today
     */
    public boolean isForToday() {
        return this.queueDate.equals(LocalDate.now());
    }

    /**
     * Checks if this is an emergency queue entry.
     *
     * @return true if priority is EMERGENCY
     */
    public boolean isEmergency() {
        return this.priority == Priority.EMERGENCY;
    }
}
