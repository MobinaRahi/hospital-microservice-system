package hospital.billingservice.dto.patientinsurance;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning patient insurance data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientInsuranceResponseDto {

    private Long id;
    private Long patientId;
    private InsuranceManagementResponseDto insurance;
    private String policyNumber;
    private LocalDate expiryDate;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
