package hospital.inventoryservice.dto.equipment;

import hospital.inventoryservice.model.enums.EquipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new equipment record.
 * Used in POST /api/v1/inventory/equipment
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code name} is required and max 200 characters</li>
 *   <li>{@code type} is required (enum value)</li>
 *   <li>{@code serialNumber} is required and must be unique, max 100 characters</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentCreateDto {

    /**
     * Name of the equipment (e.g., "Bed #101", "Ventilator A").
     * Required. Max 200 characters.
     */
    @NotBlank(message = "Equipment name is required")
    @Size(max = 200, message = "Equipment name must be at most 200 characters")
    private String name;

    /**
     * Type of equipment.
     * Required.
     */
    @NotNull(message = "Equipment type is required")
    private EquipmentType type;

    /**
     * Unique serial number from the manufacturer.
     * Required. Must be unique across all equipment. Max 100 characters.
     */
    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number must be at most 100 characters")
    private String serialNumber;

    /**
     * Model number.
     * Optional. Max 100 characters.
     */
    @Size(max = 100, message = "Model must be at most 100 characters")
    private String model;

    /**
     * Manufacturer name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Manufacturer must be at most 200 characters")
    private String manufacturer;

    /**
     * Date of purchase.
     * Optional.
     */
    private LocalDate purchaseDate;

    /**
     * Warranty expiry date.
     * Optional.
     */
    private LocalDate warrantyExpiry;

    /**
     * Current physical location (e.g., "ICU Room 3", "Warehouse B").
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String currentLocation;
}
