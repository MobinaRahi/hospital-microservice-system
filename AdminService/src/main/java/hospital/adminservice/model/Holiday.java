package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a holiday or non-working day in the hospital calendar.
 * Supports both one-time and recurring holidays.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Holidays can be one-time (specific date) or recurring (same date every year)</li>
 *   <li>Recurring holidays are checked by month and day only</li>
 *   <li>Active flag allows temporary disabling without deletion</li>
 *   <li>Used for shift scheduling and payroll calculations</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li>One-time: "Hospital Anniversary - 2026-03-21"</li>
 *   <li>Recurring: "New Year's Day - Every year on Jan 1"</li>
 *   <li>Recurring: "National Holiday - Every year on specific date"</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "holidays",
        indexes = {
                @Index(name = "idx_holiday_date", columnList = "date"),
                @Index(name = "idx_holiday_year", columnList = "year"),
                @Index(name = "idx_holiday_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Holiday extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the holiday.
     * Example: "New Year's Day", "Hospital Anniversary", "National Holiday"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Date of the holiday.
     * For recurring holidays, only month and day are used.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Year of the holiday.
     * For recurring holidays, this is the year it was created.
     */
    @Column(nullable = false)
    private Integer year;

    /**
     * Whether this holiday recurs every year.
     * If true, the holiday is checked by month and day only.
     */
    @Column(name = "is_recurring", nullable = false)
    @Builder.Default
    private Boolean isRecurring = false;

    /**
     * Whether this holiday is currently active.
     * Inactive holidays are ignored in calculations.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Checks if a given date matches this holiday.
     * For recurring holidays, compares only month and day.
     * For one-time holidays, compares the full date.
     *
     * @param target the date to check
     * @return true if the target date matches this holiday
     */
    public boolean isOnDate(LocalDate target) {
        if (target == null) return false;
        if (Boolean.TRUE.equals(isRecurring)) {
            return target.getMonth() == date.getMonth() && target.getDayOfMonth() == date.getDayOfMonth();
        }
        return target.equals(date);
    }
}
