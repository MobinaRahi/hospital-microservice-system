package hospital.coreservice.dto.doctor_schedule;

import com.hospital.coreService.dto.doctor.DoctorResponseDto;
import com.hospital.coreService.model.enums.DayOfWeek;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for sending doctor's work schedule data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * Unlike the Entity, this DTO contains full doctor information as a nested DTO.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DoctorScheduleResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the schedule.
     */
    private Long id;

    // ========== Relationships (Full object for display) ==========

    /**
     * Doctor associated with this schedule.
     * Contains full doctor information (id, name, specialty, etc.).
     */
    private DoctorResponseDto doctor;

    // ========== Schedule Information ==========

    /**
     * Day of the week for this schedule.
     * <p>Possible values: SATURDAY, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY</p>
     * <p>Note: In Iranian calendar, SATURDAY is the first day of the week.</p>
     */
    private DayOfWeek dayOfWeek;

    /**
     * Start time of the work day.
     * <p>Example: 09:00 for morning shift</p>
     */
    private LocalTime startTime;

    /**
     * End time of the work day.
     * <p>Example: 14:00 for morning shift</p>
     */
    private LocalTime endTime;

    /**
     * Duration of each appointment slot in minutes.
     * <p>Examples: 15 minutes for quick checkups, 30 minutes for specialist visits.</p>
     */
    private Integer slotDuration;

    /**
     * Physical location where the doctor sees patients.
     * <p>Example: "Room 101", "Cardiology Clinic - 2nd Floor"</p>
     */
    private String location;

    // ========== Status ==========

    /**
     * Active/Inactive status of this schedule.
     * <p>
     * - true: This schedule is active and used for appointment booking
     * - false: This schedule is inactive (e.g., doctor changed hours for this day)
     * </p>
     */
    private Boolean isActive;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this schedule was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when this schedule was last updated.
     */
    private LocalDateTime updatedAt;
}