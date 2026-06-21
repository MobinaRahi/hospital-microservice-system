package hospital.coreservice.dto.shift;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftResponseDto {

    private Long id;

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer durationHours;

    private Boolean nightShift;

    private Boolean hasExtraPay;

    private Boolean isActive;
}