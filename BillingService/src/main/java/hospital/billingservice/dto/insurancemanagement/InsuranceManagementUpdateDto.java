package hospital.billingservice.dto.insurancemanagement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing insurance plan.
 * All fields are optional — only provided fields will be updated.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceManagementUpdateDto {

    @Size(max = 200, message = "Insurance name must be at most 200 characters")
    private String name;

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

    private Boolean isActive;
}
