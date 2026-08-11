package hospital.billingservice.dto.insurancemanagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning insurance plan data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceManagementResponseDto {

    private Long id;
    private String name;
    private String code;
    private Integer coveragePercent;
    private Boolean hasDeductible;
    private Integer deductibleAmount;
    private Integer maxCoveragePerYear;
    private String phone;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
