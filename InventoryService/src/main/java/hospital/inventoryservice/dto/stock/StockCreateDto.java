package hospital.inventoryservice.dto.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new stock record.
 * Used in POST /api/v1/inventory/stocks
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code drugId} is required (must reference existing drug)</li>
 *   <li>{@code quantity} is required and must be non-negative</li>
 *   <li>{@code batchNumber} is required and max 100 characters</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCreateDto {

    /**
     * ID of the drug this stock belongs to.
     * Required. Must reference an existing drug.
     */
    @NotNull(message = "Drug ID is required")
    private Long drugId;

    /**
     * Batch/lot number from the manufacturer.
     * Required. Max 100 characters.
     */
    @NotBlank(message = "Batch number is required")
    @Size(max = 100, message = "Batch number must be at most 100 characters")
    private String batchNumber;

    /**
     * Initial quantity in stock.
     * Required. Must be non-negative.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    /**
     * Minimum stock level — triggers reorder alert when quantity falls below this.
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Minimum stock level must be non-negative")
    private Integer minStockLevel;

    /**
     * Maximum stock level — for capacity planning.
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Maximum stock level must be non-negative")
    private Integer maxStockLevel;

    /**
     * Physical storage location (e.g., "Warehouse A, Shelf 3").
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;

    /**
     * Expiry date of this batch.
     * Optional but recommended for pharmaceutical products.
     */
    private LocalDate expiryDate;
}
