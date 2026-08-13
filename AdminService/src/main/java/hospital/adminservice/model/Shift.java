package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a work shift definition in the hospital.
 * Defines shift timings, duration, and special conditions.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each shift must have a unique code</li>
 *   <li>Shifts can be day shifts or night shifts</li>
 *   <li>Weekend shifts may have different pay rates</li>
 *   <li>Extra pay percent is applied to base salary for special shifts</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li>Morning Shift: 08:00 - 16:00 (8 hours)</li>
 *   <li>Evening Shift: 16:00 - 00:00 (8 hours)</li>
 *   <li>Night Shift: 00:00 - 08:00 (8 hours, nightShift=true)</li>
 * </ul>
 *
 * <p><strong>Relationships:</strong></p>
 * <ul>
 *   <li>One-to-Many with EmployeeShift (shift assignments)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "shifts",
        indexes = {
                @Index(name = "idx_shift_code", columnList = "code", unique = true),
                @Index(name = "idx_shift_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Shift extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the shift.
     * Example: "Morning Shift", "Night Shift", "Weekend Shift"
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Unique code identifier for the shift.
     * Example: "MORNING", "EVENING", "NIGHT"
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Shift start time.
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * Shift end time.
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * Duration of the shift in hours.
     * Example: 8, 12, 24
     */
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    /**
     * Whether this is a night shift.
     * Night shifts typically have higher pay rates.
     */
    @Column(name = "night_shift", nullable = false)
    @Builder.Default
    private Boolean nightShift = false;

    /**
     * Whether this shift applies to weekends.
     * Weekend shifts may have different pay rates.
     */
    @Column(name = "weekend_shift", nullable = false)
    @Builder.Default
    private Boolean weekendShift = false;

    /**
     * Extra pay percentage for this shift.
     * Applied on top of base salary.
     * Example: 25 means 25% extra pay
     */
    @Column(name = "extra_pay_percent", nullable = false)
    @Builder.Default
    private Integer extraPayPercent = 0;

    /**
     * Whether this shift definition is active.
     * Inactive shifts cannot be assigned to employees.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * List of employee shift assignments for this shift.
     * Lazy-loaded to improve performance.
     */
    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    @Builder.Default
    private List<EmployeeShift> employeeShifts = new ArrayList<>();
}
