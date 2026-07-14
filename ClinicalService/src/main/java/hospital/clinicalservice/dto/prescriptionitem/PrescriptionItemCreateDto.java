package hospital.clinicalservice.dto.prescriptionitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionItemCreateDto {

    @NotNull(message = "Prescription ID is required")
    private Long prescriptionId;

    private Long drugId;

    @NotBlank(message = "Drug name is required")
    @Size(max = 200, message = "Drug name must not exceed 200 characters")
    private String drugName;

    @NotBlank(message = "Dosage is required")
    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency;

    @Size(max = 50, message = "Duration must not exceed 50 characters")
    private String duration;

    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    private String instructions;

    private Integer quantity;

    private boolean substitutionAllowed;
}
