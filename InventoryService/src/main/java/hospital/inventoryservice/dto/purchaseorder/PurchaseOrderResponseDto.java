package hospital.inventoryservice.dto.purchaseorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning purchase order data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full order details</li>
 *   <li>Supplier information (nested)</li>
 *   <li>Order items (nested list)</li>
 *   <li>Total amount (computed)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderResponseDto {

    /**
     * Unique ID of the purchase order.
     */
    private Long id;

    /**
     * Supplier information (nested).
     */
    private SupplierResponseDto supplier;

    /**
     * Date the order was created.
     */
    private LocalDate orderDate;

    /**
     * Expected delivery date.
     */
    private LocalDate expectedDeliveryDate;

    /**
     * Actual delivery date (null if not yet received).
     */
    private LocalDate actualDeliveryDate;

    /**
     * Current status of the purchase order.
     */
    private PurchaseOrderStatus status;

    /**
     * Total amount (sum of all items).
     */
    private BigDecimal totalAmount;

    /**
     * Additional notes.
     */
    private String notes;

    /**
     * ID of the user who created this order.
     */
    private Long createdByUser;

    /**
     * List of items in this order.
     */
    private List<PurchaseOrderItemResponseDto> items;

    /**
     * When this order was created.
     */
    private LocalDateTime createdAt;

    /**
     * When this order was last updated.
     */
    private LocalDateTime updatedAt;
}
