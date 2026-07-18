package hospital.clinicalservice.dto.prescriptionitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating a prescription item.
 *
 * @author Mobina
 */
public class PrescriptionItemUpdateDto {

    @NotNull(message = "Item ID is required for update")
    private Long id;

    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency;

    @Size(max = 50, message = "Duration must not exceed 50 characters")
    private String duration;

    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    private String instructions;

    private Integer quantity;

    private Boolean substitutionAllowed;
}
