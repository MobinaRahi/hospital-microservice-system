package hospital.adminservice.dto.employeeshift;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for updating an employee shift assignment.
 * Used to record actual attendance times and presence status.
 *
 * <p><strong>Typical Usage:</strong></p>
 * <ul>
 *   <li>Record clock-in: {@code actualStart}</li>
 *   <li>Record clock-out: {@code actualEnd}</li>
 *   <li>Mark as present: {@code isPresent = true}</li>
 *   <li>Mark as absent: {@code isPresent = false}</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
public class EmployeeShiftUpdateDto {

    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Boolean isPresent;
    private String notes;
}
