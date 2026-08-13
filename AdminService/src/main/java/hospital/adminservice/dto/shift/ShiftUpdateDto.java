package hospital.adminservice.dto.shift;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;

/**
 * DTO for updating an existing shift definition.
 * All fields are optional - only provided fields will be updated.
 *
 * @author MobinaRahi
 */
@Data
public class ShiftUpdateDto {

    @Size(max = 100, message = "Shift name must be at most 100 characters")
    private String name;

    private LocalTime startTime;
    private LocalTime endTime;

    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    private Boolean nightShift;
    private Boolean weekendShift;

    @Positive(message = "Extra pay percent must be positive")
    private Integer extraPayPercent;

    private Boolean isActive;
}
