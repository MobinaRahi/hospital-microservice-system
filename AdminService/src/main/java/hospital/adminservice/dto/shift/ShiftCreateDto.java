package hospital.adminservice.dto.shift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

/**
 * DTO for creating a new shift definition.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Shift name (max 100 characters)</li>
 *   <li>{@code code} - Unique shift code (max 50 characters)</li>
 *   <li>{@code startTime} - Shift start time</li>
 *   <li>{@code endTime} - Shift end time</li>
 *   <li>{@code durationHours} - Duration in hours (must be positive)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code nightShift} - Whether this is a night shift (default: false)</li>
 *   <li>{@code weekendShift} - Whether this applies to weekends (default: false)</li>
 *   <li>{@code extraPayPercent} - Extra pay percentage (default: 0)</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {
 *   "name": "Morning Shift",
 *   "code": "MORNING",
 *   "startTime": "08:00",
 *   "endTime": "16:00",
 *   "durationHours": 8,
 *   "nightShift": false,
 *   "weekendShift": false,
 *   "extraPayPercent": 0
 * }
 * </pre>
 *
 * @author MobinaRahi
 */
@Data
@SuperBuilder
public class ShiftCreateDto {

    @NotBlank(message = "Shift name is required")
    @Size(max = 100, message = "Shift name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Shift code is required")
    @Size(max = 50, message = "Shift code must be at most 50 characters")
    private String code;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Duration hours is required")
    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    private Boolean nightShift;
    private Boolean weekendShift;

    @Positive(message = "Extra pay percent must be positive")
    private Integer extraPayPercent;
}
