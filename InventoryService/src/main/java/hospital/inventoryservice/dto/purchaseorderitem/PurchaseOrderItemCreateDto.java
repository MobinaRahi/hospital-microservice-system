package hospital.inventoryservice.dto.purchaseorderitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new purchase order item.
 * Used as nested DTO in PurchaseOrderCreateDto.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code drugId} is required (must reference existing drug)</li>
 *   <li>{@code quantity} is required and must be positive</li>
 *   <li>{@code unitPrice} is required and must be positive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemCreateDto {

    /**
     * ID of the drug being ordered.
     * Required. Must reference an existing drug.
     */
    @NotNull(message = "Drug ID is required")
    private Long drugId;

    /**
     * Quantity ordered.
     * Required. Must be positive.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /**
     * Price per unit.
     * Required. Must be positive.
     */
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    /**
     * Description/strength from the supplier catalog.
     * Optional. Max 500 characters.
     */
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
}
