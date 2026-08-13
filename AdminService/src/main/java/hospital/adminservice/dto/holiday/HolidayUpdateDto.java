package hospital.adminservice.dto.holiday;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating an existing holiday.
 * Only name, recurring flag, and active status can be updated.
 * Date and year cannot be changed after creation.
 *
 * @author MobinaRahi
 */
@Data
public class HolidayUpdateDto {

    @Size(max = 200, message = "Holiday name must be at most 200 characters")
    private String name;

    private Boolean isRecurring;
    private Boolean isActive;
}
