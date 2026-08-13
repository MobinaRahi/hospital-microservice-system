package hospital.labservice.dto.labtest;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.model.enums.SampleType;
import hospital.labservice.model.enums.TestCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning lab test data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabTestResponseDto {

    private Long id;
    private String code;
    private String name;
    private TestCategory category;
    private String loincCode;
    private String normalRange;
    private String unit;
    private BigDecimal price;
    private String preparationInstructions;
    private Boolean requiresSample;
    private SampleType sampleType;
    private Integer turnaroundHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
