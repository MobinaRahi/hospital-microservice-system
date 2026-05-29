package hospital.coreservice.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new room.
 * <p>
 * This DTO contains only the fields that are required for creating a room.
 * Fields like isOccupied are managed automatically (default = false).
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class RoomCreateDto {

    // ========== Basic Information ==========

    /**
     * Unique room or bed number.
     * <p>Example format: "A-101", "B-202", "ICU-01"</p>
     */
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    /**
     * ID of the department this room belongs to.
     * <p>Optional - can be null if not yet assigned.</p>
     */
    private Long departmentId;

    /**
     * Maximum number of patients this room can accommodate.
     * <p>- For single-bed rooms: 1</p>
     * <p>- For shared wards: 2, 4, 6, etc.</p>
     */
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    /**
     * Room features and amenities.
     * <p>Example: "Oxygen, Monitor, TV, Private Bathroom"</p>
     */
    private String features;
}