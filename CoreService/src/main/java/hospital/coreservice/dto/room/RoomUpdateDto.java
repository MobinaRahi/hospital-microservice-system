package hospital.coreservice.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating an existing room.
 *
 * @author Mobina
 */
public class RoomUpdateDto {

    @NotNull(message = "Room ID is required for update")
    private Long id;

    private String roomNumber;

    private Long departmentId;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private String features;

    private Boolean isOccupied;
}