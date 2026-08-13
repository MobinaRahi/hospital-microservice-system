package hospital.labservice.dto.labtechnician;

import hospital.labservice.model.enums.LabShift;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new lab technician.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code userId} - User ID from AuthService</li>
 *   <li>{@code firstName} - Technician's first name (max 100 characters)</li>
 *   <li>{@code lastName} - Technician's last name (max 100 characters)</li>
 *   <li>{@code employeeCode} - Unique employee code (max 50 characters)</li>
 *   <li>{@code shift} - Assigned work shift</li>
 *   <li>{@code hireDate} - Date when technician was hired</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code specialization} - Area of specialization</li>
 *   <li>{@code certificationNumber} - Certification number</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTechnicianCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @NotBlank(message = "Employee code is required")
    @Size(max = 50, message = "Employee code must be at most 50 characters")
    private String employeeCode;

    @Size(max = 100, message = "Specialization must be at most 100 characters")
    private String specialization;

    @Size(max = 50, message = "Certification number must be at most 50 characters")
    private String certificationNumber;

    @NotNull(message = "Shift is required")
    private LabShift shift;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
}
