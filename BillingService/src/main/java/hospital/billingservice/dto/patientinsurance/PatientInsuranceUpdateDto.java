package hospital.billingservice.dto.patientinsurance;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating an existing patient insurance record.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientInsuranceUpdateDto {

    @Size(max = 100, message = "Policy number must be at most 100 characters")
    private String policyNumber;

    private LocalDate expiryDate;

    private Boolean isPrimary;
}
