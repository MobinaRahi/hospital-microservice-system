package hospital.adminservice.dto.employeeshift;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for creating a new employee shift assignment.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code employeeId} - Employee ID from BillingService</li>
 *   <li>{@code shiftId} - Shift definition ID</li>
 *   <li>{@code date} - Date of the shift assignment</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code actualStart} - Actual clock-in time</li>
 *   <li>{@code actualEnd} - Actual clock-out time</li>
 *   <li>{@code isPresent} - Whether employee worked the shift (default: false)</li>
 *   <li>{@code notes} - Additional notes</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
public class EmployeeShiftCreateDto {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Shift ID is required")
    private Long shiftId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Boolean isPresent;
    private String notes;
}
