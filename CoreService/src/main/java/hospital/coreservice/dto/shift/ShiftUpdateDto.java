package hospital.coreservice.dto.shift;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for updating an existing work shift.
 * <p>
 * All fields are optional except the ID, allowing partial updates.
 * Only fields that are provided will be updated.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class ShiftUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the shift to be updated.
     */
    @NotNull(message = "Shift ID is required for update")
    private Long id;

    // ========== Basic Information (Optional) ==========

    /**
     * Name of the work shift.
     * <p>Common values: "Morning", "Evening", "Night", "On-Call"</p>
     */
    private String name;

    /**
     * Start time of the shift.
     */
    private LocalTime startTime;

    /**
     * End time of the shift.
     */
    private LocalTime endTime;

    // ========== Shift Specifications (Optional) ==========

    /**
     * Duration of the shift in hours.
     */
    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    /**
     * Indicates whether this is a night shift.
     */
    private Boolean nightShift;

    /**
     * Indicates whether this shift includes extra pay/overtime.
     */
    private Boolean hasExtraPay;

    // ========== Status (Optional) ==========

    /**
     * Active/Inactive status of the shift.
     * <p>- true: Shift is active and available for use</p>
     * <p>- false: Shift is inactive (deprecated/removed)</p>
     */
    private Boolean isActive;
}