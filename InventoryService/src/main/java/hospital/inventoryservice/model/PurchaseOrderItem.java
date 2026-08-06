package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a line item in a purchase order.
 * Each item references a specific drug and specifies quantity and unit price.
 *
 * <p><strong>Subtotal Calculation:</strong></p>
 * <pre>
 * subtotal = unitPrice × receivedQuantity
 * </pre>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "purchase_order_items",
        indexes = {
                @Index(name = "idx_poi_order", columnList = "purchase_order_id"),
                @Index(name = "idx_poi_drug", columnList = "drug_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PurchaseOrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The purchase order this item belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    /**
     * The drug being ordered.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    /**
     * Quantity ordered.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Price per unit.
     */
    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Quantity actually received (may be less than ordered for partial deliveries).
     */
    @Column(name = "received_quantity")
    @Builder.Default
    private Integer receivedQuantity = 0;

    /**
     * Description/strength from the supplier catalog.
     */
    @Column(length = 500)
    private String description;

    /**
     * Calculates the subtotal for this item (unitPrice × receivedQuantity).
     */
    public BigDecimal getSubtotal() {
        if (unitPrice == null || receivedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(receivedQuantity));
    }
}
