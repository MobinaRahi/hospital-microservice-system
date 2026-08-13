package hospital.labservice.dto.labtest;

import hospital.labservice.model.enums.SampleType;
import hospital.labservice.model.enums.TestCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new lab test.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code code} - Unique test code (max 50 characters)</li>
 *   <li>{@code name} - Test name (max 200 characters)</li>
 *   <li>{@code category} - Test category</li>
 *   <li>{@code price} - Test price (must be positive)</li>
 *   <li>{@code turnaroundHours} - Expected result delivery time in hours</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code loincCode} - LOINC code for standardization</li>
 *   <li>{@code normalRange} - Normal reference range</li>
 *   <li>{@code unit} - Unit of measurement</li>
 *   <li>{@code preparationInstructions} - Patient preparation instructions</li>
 *   <li>{@code requiresSample} - Whether test requires sample collection</li>
 *   <li>{@code sampleType} - Type of sample required</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestCreateDto {

    @NotBlank(message = "Test code is required")
    @Size(max = 50, message = "Test code must be at most 50 characters")
    private String code;

    @NotBlank(message = "Test name is required")
    @Size(max = 200, message = "Test name must be at most 200 characters")
    private String name;

    @NotNull(message = "Test category is required")
    private TestCategory category;

    @Size(max = 20, message = "LOINC code must be at most 20 characters")
    private String loincCode;

    @Size(max = 100, message = "Normal range must be at most 100 characters")
    private String normalRange;

    @Size(max = 50, message = "Unit must be at most 50 characters")
    private String unit;

    @NotNull(message = "Test price is required")
    @Positive(message = "Test price must be positive")
    private BigDecimal price;

    @Size(max = 500, message = "Preparation instructions must be at most 500 characters")
    private String preparationInstructions;

    private Boolean requiresSample;
    private SampleType sampleType;

    @NotNull(message = "Turnaround hours is required")
    @Positive(message = "Turnaround hours must be positive")
    private Integer turnaroundHours;
}
