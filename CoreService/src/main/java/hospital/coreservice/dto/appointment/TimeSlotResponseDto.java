package hospital.coreservice.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO for appointment response data.
 *
 * @author Mobina
 */
public class TimeSlotResponseDto {
    private LocalTime startTime;
    private LocalTime endTime;
}