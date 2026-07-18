package hospital.clinicalservice.dto.triage;

import hospital.clinicalservice.model.enums.TriageLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating a triage assessment.
 *
 * @author Mobina
 */
public class TriageUpdateDto {

    @NotNull(message = "Triage ID is required for update")
    private Long id;

    private TriageLevel level;

    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters")
    private String symptoms;

    @Size(max = 1000, message = "Blood pressure must not exceed 1000 characters")
    private String bloodPressure;

    private Long heartRate;

    private Long temperature;

    private String consciousness;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}
