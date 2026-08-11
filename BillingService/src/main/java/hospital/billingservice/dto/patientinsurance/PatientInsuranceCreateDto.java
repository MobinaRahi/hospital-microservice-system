package hospital.billingservice.dto.patientinsurance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new patient insurance record.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code patientId} is required</li>
 *   <li>{@code insuranceId} is required</li>
 *   <li>{@code policyNumber} is required and max 100 characters</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientInsuranceCreateDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Insurance ID is required")
    private Long insuranceId;

    @NotBlank(message = "Policy number is required")
    @Size(max = 100, message = "Policy number must be at most 100 characters")
    private String policyNumber;

    private LocalDate expiryDate;

    @Builder.Default
    private Boolean isPrimary = false;
}
