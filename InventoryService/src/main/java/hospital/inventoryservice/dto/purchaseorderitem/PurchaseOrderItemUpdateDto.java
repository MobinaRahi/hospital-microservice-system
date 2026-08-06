package hospital.inventoryservice.dto.purchaseorderitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing purchase order item.
 * Used in PUT /api/v1/inventory/purchase-order-items/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code drugId} cannot be changed after creation (remove and add new item instead)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemUpdateDto {

    /**
     * Updated quantity.
     * Optional. Must be positive.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /**
     * Updated price per unit.
     * Optional. Must be positive.
     */
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    /**
     * Quantity actually received (may be less than ordered for partial deliveries).
     * Optional. Must be non-negative.
     */
    @Min(value = 0, message = "Received quantity must be non-negative")
    private Integer receivedQuantity;
}
