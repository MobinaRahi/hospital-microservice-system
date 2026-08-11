package hospital.billingservice.dto.employee;

import hospital.billingservice.model.enums.EmployeePosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new employee record.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code userId} is required (links to AuthService user)</li>
 *   <li>{@code employeeCode} is required and max 50 characters</li>
 *   <li>{@code firstName} and {@code lastName} are required</li>
 *   <li>{@code position} is required</li>
 *   <li>{@code hireDate} is required</li>
 *   <li>{@code baseSalary} must be positive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Employee code is required")
    @Size(max = 50, message = "Employee code must be at most 50 characters")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @NotNull(message = "Employee position is required")
    private EmployeePosition position;

    @Size(max = 200, message = "Department must be at most 200 characters")
    private String department;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    @Size(max = 50, message = "Bank account must be at most 50 characters")
    private String bankAccount;
}
