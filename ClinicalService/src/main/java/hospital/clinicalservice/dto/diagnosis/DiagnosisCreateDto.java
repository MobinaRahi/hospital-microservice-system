package hospital.clinicalservice.dto.diagnosis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisCreateDto {

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    @NotBlank(message = "ICD-10 code is required")
    @Size(max = 10, message = "ICD-10 code must not exceed 10 characters")
    private String icd10Code;

    @NotBlank(message = "Diagnosis name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private boolean primary;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}
