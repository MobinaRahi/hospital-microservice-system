package hospital.coreservice.dto.shift;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftUpdateDto {

    @NotNull(message = "Shift ID is required for update")
    private Long id;

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    private Boolean nightShift;

    private Boolean hasExtraPay;

    private Boolean isActive;
}