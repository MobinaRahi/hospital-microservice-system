package hospital.coreservice.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for creating a new room.
 *
 * @author Mobina
 */
public class RoomCreateDto {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    private Long departmentId;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private String features;
}