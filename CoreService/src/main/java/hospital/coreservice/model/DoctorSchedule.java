package hospital.coreservice.model;

import hospital.coreservice.model.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

/**
 * Entity representing a doctor's weekly work schedule.
 * <p>
 * This class defines the working hours for a doctor on specific days of the week.
 * Each record represents one day's schedule (e.g., Dr. Smith works Saturdays 9 AM to 2 PM).
 * The schedule is used to determine available time slots for appointments.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "doctorScheduleEntity")
@Table(name = "doctor_schedules",
        indexes = {
                @Index(name = "idx_doc_schedule_doctor", columnList = "doctor_id"),
                @Index(name = "idx_doc_schedule_day", columnList = "day_of_week"),
                @Index(name = "idx_doc_schedule_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DoctorSchedule {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Relationships ==========
    /**
     * The doctor associated with this schedule.
     * <p>Many schedules can belong to one doctor.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // ========== Schedule Information ==========
    /**
     * Day of the week for this schedule (SATURDAY, SUNDAY, ..., FRIDAY).
     * <p>Note: In Iranian calendar, SATURDAY is the first day of the week.</p>
     */
    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    /**
     * Start time of the work day (e.g., 09:00 for morning shift).
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * End time of the work day (e.g., 14:00 for morning shift).
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * Duration of each appointment slot in minutes.
     * <p>Examples: 15 minutes for quick checkups, 30 minutes for specialist visits.</p>
     */
    @Column(name = "slot_duration", nullable = false)
    private Integer slotDuration;

    /**
     * Physical location where the doctor sees patients (room number, clinic name, etc.).
     * <p>Example: "Room 101", "Cardiology Clinic - 2nd Floor"</p>
     */
    @Column(name = "location", nullable = false, length = 100)
    private String location;

    // ========== Status ==========
    /**
     * Active/Inactive status of this schedule.
     * <p>
     * - true: This schedule is active and used for appointment booking
     * - false: This schedule is inactive (e.g., doctor changed hours for this day)
     * </p>
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}

