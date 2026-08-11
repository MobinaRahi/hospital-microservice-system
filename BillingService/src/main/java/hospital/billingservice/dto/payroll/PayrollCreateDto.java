package hospital.billingservice.dto.payroll;

import hospital.billingservice.model.enums.PayrollStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new payroll record.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code employeeId} is required</li>
 *   <li>{@code month} must be between 1 and 12</li>
 *   <li>{@code year} must be positive</li>
 *   <li>{@code baseSalary} must be positive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollCreateDto {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Positive(message = "Year must be positive")
    private Integer year;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    @Builder.Default
    private BigDecimal overtime = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal bonuses = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal deductions = BigDecimal.ZERO;

    private PayrollStatus status;
}
