package hospital.clinicalservice.dto.allergy;

import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for creating a new allergy record.
 *
 * @author Mobina
 */
public class AllergyCreateDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Allergy type is required")
    private AllergyType type;

    @NotBlank(message = "Allergen name is required")
    @Size(max = 200, message = "Allergen name must not exceed 200 characters")
    private String allergenName;

    @Size(max = 500, message = "Reaction must not exceed 500 characters")
    private String reaction;

    @NotNull(message = "Severity is required")
    private AllergySeverity severity;

    @Size(max = 500, message = "Symptoms must not exceed 500 characters")
    private String symptoms;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    private boolean active;
}
