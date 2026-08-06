package hospital.inventoryservice.dto.purchaseorderitem;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for returning purchase order item data in API responses.
 * Used in GET endpoints and as nested DTO in PurchaseOrderResponseDto.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full item details</li>
 *   <li>Drug information (nested)</li>
 *   <li>Subtotal (computed: unitPrice × receivedQuantity)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderItemResponseDto {

    /**
     * Unique ID of the item.
     */
    private Long id;

    /**
     * Drug information (nested).
     */
    private DrugResponseDto drug;

    /**
     * Quantity ordered.
     */
    private Integer quantity;

    /**
     * Price per unit.
     */
    private BigDecimal unitPrice;

    /**
     * Quantity actually received.
     */
    private Integer receivedQuantity;

    /**
     * Description from the supplier catalog.
     */
    private String description;

    /**
     * Subtotal for this item (unitPrice × receivedQuantity).
     * Computed field.
     */
    private BigDecimal subtotal;
}
