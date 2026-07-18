package hospital.clinicalservice.dto.diagnosis;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating an existing diagnosis.
 *
 * @author Mobina
 */
public class DiagnosisUpdateDto {

    @NotNull(message = "Diagnosis ID is required for update")
    private Long id;

    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean primary;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}
