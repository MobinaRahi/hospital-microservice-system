package hospital.coreservice.dto.doctor_schedule;

import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.model.enums.DayOfWeek;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
/**
 * DTO for doctor schedule response data.
 *
 * @author Mobina
 */
public class DoctorScheduleResponseDto {

    private Long id;

    private DoctorResponseDto doctor;

    private DayOfWeek dayOfWeek;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer slotDuration;

    private String location;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}