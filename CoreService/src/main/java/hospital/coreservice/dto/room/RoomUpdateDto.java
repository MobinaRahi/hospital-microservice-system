package hospital.coreservice.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing room.
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
public class RoomUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the room to be updated.
     */
    @NotNull(message = "Room ID is required for update")
    private Long id;

    // ========== Basic Information (Optional) ==========

    /**
     * Unique room or bed number.
     * <p>Example format: "A-101", "B-202", "ICU-01"</p>
     */
    private String roomNumber;

    /**
     * ID of the department this room belongs to.
     * <p>Can be null to remove the room from its current department.</p>
     */
    private Long departmentId;

    /**
     * Maximum number of patients this room can accommodate.
     */
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    /**
     * Room features and amenities.
     */
    private String features;

    // ========== Occupancy Status (Optional) ==========

    /**
     * Manually override occupancy status.
     * <p>Usually managed automatically when adding/removing patients.</p>
     */
    private Boolean isOccupied;
}