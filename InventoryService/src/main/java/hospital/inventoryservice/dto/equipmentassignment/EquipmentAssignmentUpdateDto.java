package hospital.inventoryservice.dto.equipmentassignment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for updating an existing equipment assignment.
 * Used in PUT /api/v1/inventory/equipment-assignments/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code equipmentId} cannot be changed after creation</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentAssignmentUpdateDto {

    /**
     * Updated expected return date.
     * Optional.
     */
    private LocalDateTime expectedReturnDate;

    /**
     * Actual return date (set when equipment is returned).
     * Optional.
     */
    private LocalDateTime actualReturnDate;

    /**
     * Updated notes about the assignment.
     * Optional. Max 500 characters.
     */
    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
