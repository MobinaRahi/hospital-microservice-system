package hospital.adminservice.dto.holiday;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating a new holiday.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Holiday name (max 200 characters)</li>
 *   <li>{@code date} - Holiday date</li>
 *   <li>{@code year} - Year (must be positive)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code isRecurring} - Whether holiday repeats yearly (default: false)</li>
 *   <li>{@code isActive} - Whether holiday is active (default: true)</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <pre>
 * One-time holiday:
 * {
 *   "name": "Hospital Anniversary",
 *   "date": "2026-03-21",
 *   "year": 2026,
 *   "isRecurring": false
 * }
 *
 * Recurring holiday:
 * {
 *   "name": "New Year's Day",
 *   "date": "2026-01-01",
 *   "year": 2026,
 *   "isRecurring": true
 * }
 * </pre>
 *
 * @author MobinaRahi
 */
@Data
public class HolidayCreateDto {

    @NotBlank(message = "Holiday name is required")
    @Size(max = 200, message = "Holiday name must be at most 200 characters")
    private String name;

    @NotNull(message = "Holiday date is required")
    private LocalDate date;

    @NotNull(message = "Year is required")
    @Positive(message = "Year must be positive")
    private Integer year;

    private Boolean isRecurring;
    private Boolean isActive;
}
