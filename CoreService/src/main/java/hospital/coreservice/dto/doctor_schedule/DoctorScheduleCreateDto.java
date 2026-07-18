package hospital.coreservice.dto.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
/**
 * DTO for creating a new doctor schedule.
 *
 * @author Mobina
 */
public class DoctorScheduleCreateDto {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Slot duration is required")
    @Positive(message = "Slot duration must be positive")
    private Integer slotDuration;

    @NotNull(message = "Location is required")
    private String location;
}