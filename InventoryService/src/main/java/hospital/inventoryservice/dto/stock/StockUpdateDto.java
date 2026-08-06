package hospital.inventoryservice.dto.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing stock record.
 * Used in PUT /api/v1/inventory/stocks/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code drugId} and {@code batchNumber} cannot be changed after creation</li>
 *   <li>{@code expiryDate} should not be changed (create new batch instead)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDto {

    /**
     * Updated quantity.
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    /**
     * Updated minimum stock level.
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Minimum stock level must be non-negative")
    private Integer minStockLevel;

    /**
     * Updated maximum stock level.
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Maximum stock level must be non-negative")
    private Integer maxStockLevel;

    /**
     * Updated storage location.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;
}
