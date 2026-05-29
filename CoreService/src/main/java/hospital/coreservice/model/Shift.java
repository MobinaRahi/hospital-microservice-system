package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

/**
 * Entity representing a work shift in the hospital.
 * <p>
 * This class represents a work shift (e.g., morning, evening, night) that
 * hospital staff (nurses, receptionists, etc.) work in.
 * Each shift has a start time, end time, duration, and specific properties.
 * </p>
 *
 * @author mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "shiftEntity")
@Table(name = "shifts",
        indexes = {
                @Index(name = "idx_shift_name", columnList = "name"),
                @Index(name = "idx_shift_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Shift {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Basic Information ==========
    /**
     * Name of the work shift.
     * <p>Common values: "Morning", "Evening", "Night", "On-Call"</p>
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * Start time of the shift (e.g., 08:00 for morning shift)
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * End time of the shift (e.g., 14:00 for morning shift)
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // ========== Shift Specifications ==========
    /**
     * Duration of the shift in hours (optional, can be calculated from startTime and endTime)
     * <p>Example: 6 hours for morning shift, 12 hours for night shift</p>
     */
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    /**
     * Indicates whether this is a night shift.
     * <p>Used for overtime and salary calculations</p>
     */
    @Column(name = "night_shift")
    private boolean nightShift;

    /**
     * Indicates whether this shift includes extra pay/overtime.
     * <p>Night shifts and on-call shifts typically have extra pay</p>
     */
    @Column(name = "has_extra_pay")
    private boolean hasExtraPay;

    // ========== Status ==========
    /**
     * Active/Inactive status of the shift.
     * <p>
     * - true: Shift is active and available for use
     * - false: Shift is inactive (deprecated/removed)
     * </p>
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
