package hospital.coreservice.dto.shift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftCreateDto {

    @NotBlank(message = "Shift name is required")
    private String name;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Duration hours is required")
    @Positive(message = "Duration hours must be positive")
    private Integer durationHours;

    private Boolean nightShift;

    private Boolean hasExtraPay;
}