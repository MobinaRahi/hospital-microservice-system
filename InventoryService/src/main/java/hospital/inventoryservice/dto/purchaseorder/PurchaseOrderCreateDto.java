package hospital.inventoryservice.dto.purchaseorder;

import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating a new purchase order.
 * Used in POST /api/v1/inventory/purchase-orders
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code supplierId} is required (must reference existing supplier)</li>
 *   <li>{@code orderDate} is required</li>
 *   <li>{@code items} must have at least one item</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCreateDto {

    /**
     * ID of the supplier this order is placed with.
     * Required. Must reference an existing supplier.
     */
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    /**
     * Date the order was created.
     * Required. Cannot be in the future.
     */
    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    /**
     * Expected delivery date from the supplier.
     * Optional. Must be after order date if provided.
     */
    private LocalDate expectedDeliveryDate;

    /**
     * Additional notes for the supplier.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;

    /**
     * List of items in this order.
     * Required. Must have at least one item.
     */
    @NotEmpty(message = "Purchase order must have at least one item")
    private List<PurchaseOrderItemCreateDto> items;
}
