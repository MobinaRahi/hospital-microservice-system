package hospital.coreservice.dto.shift;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for sending work shift data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class ShiftResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the shift.
     */
    private Long id;

    // ========== Basic Information ==========

    /**
     * Name of the work shift.
     * <p>Example: "Morning", "Evening", "Night", "On-Call"</p>
     */
    private String name;

    /**
     * Start time of the shift.
     * <p>Example: 08:00</p>
     */
    private LocalTime startTime;

    /**
     * End time of the shift.
     * <p>Example: 14:00</p>
     */
    private LocalTime endTime;

    // ========== Shift Specifications ==========

    /**
     * Duration of the shift in hours.
     * <p>Example: 6</p>
     */
    private Integer durationHours;

    /**
     * Indicates whether this is a night shift.
     */
    private Boolean nightShift;

    /**
     * Indicates whether this shift includes extra pay/overtime.
     */
    private Boolean hasExtraPay;

    // ========== Status ==========

    /**
     * Active/Inactive status of the shift.
     * <p>- true: Shift is active and available for use</p>
     * <p>- false: Shift is inactive (deprecated/removed)</p>
     */
    private Boolean isActive;
}