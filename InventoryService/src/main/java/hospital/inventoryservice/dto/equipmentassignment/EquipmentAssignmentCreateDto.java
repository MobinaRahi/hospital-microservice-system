package hospital.inventoryservice.dto.equipmentassignment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating a new equipment assignment.
 * Used in POST /api/v1/inventory/equipment-assignments
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code equipmentId} is required (must reference existing equipment)</li>
 *   <li>{@code assignedDate} is required</li>
 *   <li>Either {@code patientId} or {@code departmentId} must be provided (at least one)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentAssignmentCreateDto {

    /**
     * ID of the equipment being assigned.
     * Required. Must reference existing equipment.
     */
    @NotNull(message = "Equipment ID is required")
    private Long equipmentId;

    /**
     * Patient ID from CoreService.
     * Optional. If provided, equipment is assigned to a patient.
     */
    private Long patientId;

    /**
     * Department ID from CoreService.
     * Optional. If provided, equipment is assigned to a department.
     */
    private Long departmentId;

    /**
     * When the equipment was assigned.
     * Required.
     */
    @NotNull(message = "Assigned date is required")
    private LocalDateTime assignedDate;

    /**
     * Expected return date.
     * Optional. Must be after assigned date if provided.
     */
    private LocalDateTime expectedReturnDate;

    /**
     * Notes about the assignment.
     * Optional. Max 500 characters.
     */
    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
