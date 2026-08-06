package hospital.inventoryservice.model;

import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a purchase order placed with a supplier for drugs or equipment.
 *
 * <p><strong>Status Flow:</strong></p>
 * <pre>
 * PENDING → APPROVED → SENT → PARTIAL → COMPLETED
 *                                    ↑
 *                               (cancelled at any point)
 * </pre>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "purchase_orders",
        indexes = {
                @Index(name = "idx_po_supplier", columnList = "supplier_id"),
                @Index(name = "idx_po_status", columnList = "status"),
                @Index(name = "idx_po_date", columnList = "order_date")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PurchaseOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The supplier this order is placed with.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    /**
     * Date the order was created.
     */
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    /**
     * Expected delivery date from the supplier.
     */
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    /**
     * Actual delivery date (set when order is received).
     */
    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;

    /**
     * Current status of the purchase order.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PurchaseOrderStatus status = PurchaseOrderStatus.PENDING;

    /**
     * Total amount of the order (sum of all items).
     */
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Additional notes for the supplier.
     */
    @Column(length = 1000)
    private String notes;

    /**
     * ID of the user who created this order.
     */
    @Column(name = "created_by")
    private Long createdByUser;

    /**
     * Line items in this order.
     */
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();

    /**
     * Recalculates the total amount based on all items.
     */
    public void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(PurchaseOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
