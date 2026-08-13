package hospital.labservice.dto.labrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.dto.labrequestitem.LabRequestItemResponseDto;
import hospital.labservice.model.enums.RequestPriority;
import hospital.labservice.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning lab request data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabRequestResponseDto {

    private Long id;
    private String requestNumber;
    private Long patientId;
    private Long doctorId;
    private Long encounterId;
    private LocalDateTime requestDate;
    private RequestPriority priority;
    private RequestStatus status;
    private String clinicalNotes;
    private Long requestedBy;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private List<LabRequestItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
