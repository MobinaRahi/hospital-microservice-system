package hospital.labservice.dto.labrequest;

import hospital.labservice.model.enums.RequestPriority;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing lab request.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code requestNumber}, {@code patientId}, {@code doctorId}
 * and {@code items} are not updatable as they define the request identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabRequestUpdateDto {

    private RequestPriority priority;

    @Size(max = 1000, message = "Clinical notes must be at most 1000 characters")
    private String clinicalNotes;
}
