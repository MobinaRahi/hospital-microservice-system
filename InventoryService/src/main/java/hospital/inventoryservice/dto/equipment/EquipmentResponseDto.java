package hospital.inventoryservice.dto.equipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning equipment data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full equipment details</li>
 *   <li>Computed field: isWarrantyExpired</li>
 *   <li>Audit timestamps</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponseDto {

    /**
     * Unique ID of the equipment.
     */
    private Long id;

    /**
     * Name of the equipment.
     */
    private String name;

    /**
     * Type of equipment.
     */
    private EquipmentType type;

    /**
     * Unique serial number.
     */
    private String serialNumber;

    /**
     * Model number.
     */
    private String model;

    /**
     * Manufacturer name.
     */
    private String manufacturer;

    /**
     * Date of purchase.
     */
    private LocalDate purchaseDate;

    /**
     * Warranty expiry date.
     */
    private LocalDate warrantyExpiry;

    /**
     * Current status.
     */
    private EquipmentStatus status;

    /**
     * Current physical location.
     */
    private String currentLocation;

    /**
     * Whether this equipment is currently active.
     */
    private Boolean isActive;

    /**
     * Whether warranty has expired.
     * Computed field based on warrantyExpiry.
     */
    private Boolean isWarrantyExpired;

    /**
     * When this equipment was created.
     */
    private LocalDateTime createdAt;

    /**
     * When this equipment was last updated.
     */
    private LocalDateTime updatedAt;
}
