package hospital.inventoryservice.dto.equipmentassignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning equipment assignment data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full assignment details</li>
 *   <li>Equipment information (nested)</li>
 *   <li>Computed field: isActive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentAssignmentResponseDto {

    /**
     * Unique ID of the assignment.
     */
    private Long id;

    /**
     * Equipment information (nested).
     */
    private EquipmentResponseDto equipment;

    /**
     * Patient ID (null if assigned to department).
     */
    private Long patientId;

    /**
     * Department ID (null if assigned to patient).
     */
    private Long departmentId;

    /**
     * When the equipment was assigned.
     */
    private LocalDateTime assignedDate;

    /**
     * Expected return date.
     */
    private LocalDateTime expectedReturnDate;

    /**
     * Actual return date (null if still in use).
     */
    private LocalDateTime actualReturnDate;

    /**
     * ID of the user who assigned the equipment.
     */
    private Long assignedBy;

    /**
     * Notes about the assignment.
     */
    private String notes;

    /**
     * Whether this assignment is still active.
     * Computed field based on actualReturnDate.
     */
    private Boolean isActive;
}
