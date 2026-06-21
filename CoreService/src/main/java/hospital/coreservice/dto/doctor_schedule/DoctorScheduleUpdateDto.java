package hospital.coreservice.dto.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class DoctorScheduleUpdateDto {

    @NotNull(message = "Schedule ID is required for update")
    private Long id;

    private Long doctorId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @Positive(message = "Slot duration must be positive")
    private Integer slotDuration;

    private String location;
}