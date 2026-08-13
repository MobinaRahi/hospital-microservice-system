package hospital.labservice.dto.labtechnician;

import hospital.labservice.model.enums.LabShift;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing lab technician.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code userId}, {@code firstName}, {@code lastName},
 * {@code employeeCode} and {@code hireDate} are not updatable
 * as they define the technician identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTechnicianUpdateDto {

    @Size(max = 100, message = "Specialization must be at most 100 characters")
    private String specialization;

    @Size(max = 50, message = "Certification number must be at most 50 characters")
    private String certificationNumber;

    private LabShift shift;
    private Boolean isActive;
}
