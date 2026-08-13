package hospital.adminservice.dto.shift;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * DTO for returning shift data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftResponseDto {

    private Long id;
    private String name;
    private String code;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationHours;
    private Boolean nightShift;
    private Boolean weekendShift;
    private Integer extraPayPercent;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
