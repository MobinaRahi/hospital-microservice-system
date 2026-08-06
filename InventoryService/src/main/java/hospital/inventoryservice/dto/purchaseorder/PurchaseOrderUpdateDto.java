package hospital.inventoryservice.dto.purchaseorder;

import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating an existing purchase order.
 * Used in PUT /api/v1/inventory/purchase-orders/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code supplierId} and {@code orderDate} cannot be changed after creation</li>
 *   <li>Items are managed separately via item endpoints</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderUpdateDto {

    /**
     * Updated expected delivery date.
     * Optional. Must be after order date if provided.
     */
    private LocalDate expectedDeliveryDate;

    /**
     * Actual delivery date (set when order is received).
     * Optional.
     */
    private LocalDate actualDeliveryDate;

    /**
     * Updated status of the purchase order.
     * Optional. Must follow status workflow.
     */
    private PurchaseOrderStatus status;

    /**
     * Updated notes for the supplier.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;
}
