package hospital.billingservice.dto.insurancemanagement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new insurance plan.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code name} is required and max 200 characters</li>
 *   <li>{@code code} is required and max 50 characters</li>
 *   <li>{@code coveragePercent} must be between 0 and 100</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceManagementCreateDto {

    @NotBlank(message = "Insurance name is required")
    @Size(max = 200, message = "Insurance name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Insurance code is required")
    @Size(max = 50, message = "Insurance code must be at most 50 characters")
    private String code;

    @Min(value = 0, message = "Coverage percent must be at least 0")
    @Max(value = 100, message = "Coverage percent must be at most 100")
    private Integer coveragePercent;

    private Boolean hasDeductible;

    private Integer deductibleAmount;

    private Integer maxCoveragePerYear;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Size(max = 200, message = "Email must be at most 200 characters")
    private String email;
}
