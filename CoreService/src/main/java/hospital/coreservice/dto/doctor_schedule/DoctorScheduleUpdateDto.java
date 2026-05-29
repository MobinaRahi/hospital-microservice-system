package hospital.coreservice.dto.doctor_schedule;

import com.hospital.coreService.model.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for updating an existing doctor's work schedule.
 * <p>
 * All fields are optional except the ID, allowing partial updates.
 * Only fields that are provided will be updated.
 * Example: Change only the slot duration without affecting other fields.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DoctorScheduleUpdateDto {

    // ========== Primary Key (Required) ==========

    /**
     * ID of the schedule to be updated.
     * This field is required to identify the schedule.
     */
    @NotNull(message = "Schedule ID is required for update")
    private Long id;

    // ========== Relationships (Optional) ==========

    /**
     * ID of the doctor this schedule belongs to.
     * <p>Optional - can be used to reassign this schedule to another doctor.</p>
     */
    private Long doctorId;

    // ========== Schedule Information (Optional) ==========

    /**
     * Day of the week for this schedule.
     * <p>Possible values: SATURDAY, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY</p>
     * <p>Note: In Iranian calendar, SATURDAY is the first day of the week.</p>
     * <p>Optional for partial updates.</p>
     */
    private DayOfWeek dayOfWeek;

    /**
     * Start time of the work day.
     * <p>Example: 09:00 for morning shift</p>
     * <p>Optional for partial updates.</p>
     */
    private LocalTime startTime;

    /**
     * End time of the work day.
     * <p>Example: 14:00 for morning shift</p>
     * <p>Optional for partial updates.</p>
     */
    private LocalTime endTime;

    /**
     * Duration of each appointment slot in minutes.
     * <p>Examples: 15 minutes for quick checkups, 30 minutes for specialist visits.</p>
     * <p>Optional for partial updates.</p>
     */
    @Positive(message = "Slot duration must be positive")
    private Integer slotDuration;

    /**
     * Physical location where the doctor sees patients.
     * <p>Example: "Room 101", "Cardiology Clinic - 2nd Floor"</p>
     * <p>Optional for partial updates.</p>
     */
    private String location;
}