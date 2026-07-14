package hospital.clinicalservice.dto.prescription;

import hospital.clinicalservice.model.enums.PrescriptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PrescriptionUpdateDto {

    @NotNull(message = "Prescription ID is required for update")
    private Long id;

    private PrescriptionStatus status;

    private LocalDate expiryDate;

    private String notes;
}
