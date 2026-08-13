package hospital.labservice.dto.labtechnician;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.model.enums.LabShift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning lab technician data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabTechnicianResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String employeeCode;
    private String specialization;
    private String certificationNumber;
    private LabShift shift;
    private Boolean isActive;
    private LocalDate hireDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
