package hospital.clinicalservice.dto.observation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating an existing observation.
 *
 * @author Mobina
 */
public class ObservationUpdateDto {

    @NotNull(message = "Observation ID is required for update")
    private Long id;

    private Double valueNumeric;

    @Size(max = 200, message = "Text value must not exceed 200 characters")
    private String valueText;

    private Boolean abnormal;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
