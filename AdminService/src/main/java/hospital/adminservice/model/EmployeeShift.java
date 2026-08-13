package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an assignment of an employee to a specific shift on a specific date.
 * Tracks actual attendance times and presence status.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each employee can be assigned to one shift per day</li>
 *   <li>Actual start/end times are recorded when employee clocks in/out</li>
 *   <li>isPresent flag tracks whether employee actually worked the shift</li>
 *   <li>Used for payroll calculation and attendance tracking</li>
 * </ul>
 *
 * <p><strong>Workflow:</strong></p>
 * <ol>
 *   <li>Shift is scheduled for employee (isPresent=false)</li>
 *   <li>Employee clocks in (actualStart recorded)</li>
 *   <li>Employee clocks out (actualEnd recorded)</li>
 *   <li>isPresent set to true</li>
 * </ol>
 *
 * <p><strong>Relationships:</strong></p>
 * <ul>
 *   <li>Many-to-One with Shift (the shift definition)</li>
 *   <li>References employeeId from BillingService</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "employee_shifts",
        indexes = {
                @Index(name = "idx_emp_shift_emp", columnList = "employee_id"),
                @Index(name = "idx_emp_shift_date", columnList = "date"),
                @Index(name = "idx_emp_shift_present", columnList = "is_present")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeShift extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employee ID from BillingService.
     * Links to the employee record.
     */
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    /**
     * The shift definition this assignment is for.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    /**
     * Date of the shift assignment.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Actual clock-in time.
     * Null if employee hasn't clocked in yet.
     */
    @Column(name = "actual_start")
    private LocalDateTime actualStart;

    /**
     * Actual clock-out time.
     * Null if employee hasn't clocked out yet.
     */
    @Column(name = "actual_end")
    private LocalDateTime actualEnd;

    /**
     * Whether the employee actually worked this shift.
     * Set to true when employee completes the shift.
     */
    @Column(name = "is_present", nullable = false)
    @Builder.Default
    private Boolean isPresent = false;

    /**
     * Additional notes about this shift assignment.
     * Example: "Overtime approved", "Called in sick"
     */
    @Column(length = 500)
    private String notes;

    /**
     * Marks the employee as present and records actual times.
     *
     * @param start clock-in time
     * @param end   clock-out time
     */
    public void markPresent(LocalDateTime start, LocalDateTime end) {
        this.isPresent = true;
        this.actualStart = start;
        this.actualEnd = end;
    }

    /**
     * Marks the employee as absent for this shift.
     * Clears actual times.
     */
    public void markAbsent() {
        this.isPresent = false;
        this.actualStart = null;
        this.actualEnd = null;
    }
}
