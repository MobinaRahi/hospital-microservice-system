package hospital.clinicalservice.dto.prescription;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PrescriptionCreateDto {

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private LocalDate expiryDate;

    private String notes;
}
