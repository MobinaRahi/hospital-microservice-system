package hospital.inventoryservice.dto.equipment;

import hospital.inventoryservice.model.enums.EquipmentStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating an existing equipment record.
 * Used in PUT /api/v1/inventory/equipment/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code serialNumber} should not be changed (unique identifier)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentUpdateDto {

    /**
     * Updated name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Equipment name must be at most 200 characters")
    private String name;

    /**
     * Updated model number.
     * Optional. Max 100 characters.
     */
    @Size(max = 100, message = "Model must be at most 100 characters")
    private String model;

    /**
     * Updated manufacturer name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Manufacturer must be at most 200 characters")
    private String manufacturer;

    /**
     * Updated warranty expiry date.
     * Optional.
     */
    private LocalDate warrantyExpiry;

    /**
     * Updated current status.
     * Optional.
     */
    private EquipmentStatus status;

    /**
     * Updated current physical location.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String currentLocation;

    /**
     * Whether this equipment is currently active.
     * Optional. Used to activate/deactivate equipment.
     */
    private Boolean isActive;
}
