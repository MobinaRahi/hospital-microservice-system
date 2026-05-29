package hospital.coreservice.dto.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for creating a new doctor's work schedule.
 * <p>
 * This DTO is used when defining a doctor's working hours for a specific day of the week.
 * Example: Dr. Smith works on Saturdays from 9 AM to 2 PM with 15-minute slots.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DoctorScheduleCreateDto {

    // ========== Relationships (Just IDs) ==========

    /**
     * ID of the doctor this schedule belongs to.
     * Must reference an existing doctor in the database.
     */
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    // ========== Schedule Information ==========

    /**
     * Day of the week for this schedule.
     * <p>Possible values: SATURDAY, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY</p>
     * <p>Note: In Iranian calendar, SATURDAY is the first day of the week.</p>
     */
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    /**
     * Start time of the work day.
     * <p>Example: 09:00 for morning shift</p>
     */
    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    /**
     * End time of the work day.
     * <p>Example: 14:00 for morning shift</p>
     */
    @NotNull(message = "End time is required")
    private LocalTime endTime;

    /**
     * Duration of each appointment slot in minutes.
     * <p>Examples: 15 minutes for quick checkups, 30 minutes for specialist visits.</p>
     */
    @NotNull(message = "Slot duration is required")
    @Positive(message = "Slot duration must be positive")
    private Integer slotDuration;

    /**
     * Physical location where the doctor sees patients.
     * <p>Example: "Room 101", "Cardiology Clinic - 2nd Floor"</p>
     */
    @NotNull(message = "Location is required")
    private String location;
}