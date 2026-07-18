package hospital.clinicalservice.dto.observation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for creating a new observation with LOINC code.
 *
 * @author Mobina
 */
public class ObservationCreateDto {

    private Long encounterId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Recorded by (user ID) is required")
    private Long recordedBy;

    @NotBlank(message = "LOINC code is required")
    @Size(max = 20, message = "LOINC code must not exceed 20 characters")
    private String loincCode;

    @NotBlank(message = "Observation name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private Double valueNumeric;

    @Size(max = 200, message = "Text value must not exceed 200 characters")
    private String valueText;

    @Size(max = 30, message = "Unit must not exceed 30 characters")
    private String unit;

    private Double referenceRangeLow;

    private Double referenceRangeHigh;

    private boolean abnormal;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
