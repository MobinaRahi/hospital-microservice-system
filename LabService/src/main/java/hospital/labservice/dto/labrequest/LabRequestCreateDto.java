package hospital.labservice.dto.labrequest;

import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.model.enums.RequestPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for creating a new lab request.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code requestNumber} - Unique request number (max 50 characters)</li>
 *   <li>{@code patientId} - Patient ID from CoreService</li>
 *   <li>{@code doctorId} - Doctor ID from CoreService</li>
 *   <li>{@code items} - List of test items (must have at least one item)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code encounterId} - Encounter ID from ClinicalService</li>
 *   <li>{@code requestDate} - Request date/time (defaults to now)</li>
 *   <li>{@code priority} - Request priority (defaults to ROUTINE)</li>
 *   <li>{@code clinicalNotes} - Clinical notes for lab technician</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabRequestCreateDto {

    @NotBlank(message = "Request number is required")
    @Size(max = 50, message = "Request number must be at most 50 characters")
    private String requestNumber;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long encounterId;
    private LocalDateTime requestDate;

    private RequestPriority priority;

    @Size(max = 1000, message = "Clinical notes must be at most 1000 characters")
    private String clinicalNotes;

    @NotEmpty(message = "At least one test item is required")
    @Valid
    private List<LabRequestItemCreateDto> items;
}
