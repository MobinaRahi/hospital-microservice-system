package hospital.adminservice.dto.bed;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for returning bed data in API responses.
 * Includes computed field {@code available} for convenience.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BedResponseDto {

    private Long id;
    private String bedNumber;
    private Long departmentId;
    private BedType type;
    private BedStatus status;

    /**
     * Patient ID from CoreService (when bed is occupied).
     */
    private Long currentPatientId;

    /**
     * Admission ID from CoreService (when bed is occupied).
     */
    private Long currentAdmissionId;

    private LocalDateTime assignedAt;
    private LocalDateTime expectedDischargeDate;
    private String notes;

    /**
     * Computed field - true if bed is currently available.
     */
    private Boolean available;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
