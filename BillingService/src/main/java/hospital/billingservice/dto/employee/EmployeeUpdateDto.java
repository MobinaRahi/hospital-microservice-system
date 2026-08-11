package hospital.billingservice.dto.employee;

import hospital.billingservice.model.enums.EmployeePosition;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing employee record.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {

    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    private EmployeePosition position;

    @Size(max = 200, message = "Department must be at most 200 characters")
    private String department;

    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    @Size(max = 50, message = "Bank account must be at most 50 characters")
    private String bankAccount;

    private Boolean isActive;
}
