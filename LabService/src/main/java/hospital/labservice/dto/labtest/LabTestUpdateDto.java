package hospital.labservice.dto.labtest;

import hospital.labservice.model.enums.SampleType;
import hospital.labservice.model.enums.TestCategory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing lab test.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code code} and {@code category} are not updatable
 * as they are part of the test identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestUpdateDto {

    @Size(max = 200, message = "Test name must be at most 200 characters")
    private String name;

    private TestCategory category;

    @Size(max = 20, message = "LOINC code must be at most 20 characters")
    private String loincCode;

    @Size(max = 100, message = "Normal range must be at most 100 characters")
    private String normalRange;

    @Size(max = 50, message = "Unit must be at most 50 characters")
    private String unit;

    @Positive(message = "Test price must be positive")
    private BigDecimal price;

    @Size(max = 500, message = "Preparation instructions must be at most 500 characters")
    private String preparationInstructions;

    private Boolean requiresSample;
    private SampleType sampleType;

    @Positive(message = "Turnaround hours must be positive")
    private Integer turnaroundHours;

    private Boolean isActive;
}
