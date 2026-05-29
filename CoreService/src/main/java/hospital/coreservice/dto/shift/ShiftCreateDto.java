package hospital.coreservice.dto.shift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for creating a new work shift.
 * <p>
 * This DTO contains only the fields that are required for creating a shift.
 * Field isActive is managed automatically (default = true).
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class ShiftCreateDto {

    // ========== Basic Information ==========

    /**
     * Name of the work shift.
     * <p>Common values: "Morning", "Evening", "Night", "On-Call"</p>
     */
    @NotBlank(message = "Shift name is required")
    private String name;

    /**
     * Start time of the shift.
     * <p>Example: 08:00 for morning shift</p>
     */
    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    /**
     * End time of the shift.
     * <p>Example: 14:00 for morning shift</p>
     */
    @NotNull(message = "End time is required")
    private LocalTime endTime;

    // ========== Shift Specifications ==========

    /**
     * Duration of the shift in hours.
     * <p>Example: 6 hours for morning shift, 12 hours for night shift</p>
     */
    @NotNull(message = "Duration hours is required")
    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    /**
     * Indicates whether this is a night shift.
     * <p>Used for overtime and salary calculations</p>
     */
    private Boolean nightShift;

    /**
     * Indicates whether this shift includes extra pay/overtime.
     * <p>Night shifts and on-call shifts typically have extra pay</p>
     */
    private Boolean hasExtraPay;
}