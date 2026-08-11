package hospital.billingservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a line item on an invoice.
 * Each item references a service from the ServiceCatalog (by code)
 * and has quantity, unit price, and total price.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>totalPrice = unitPrice × quantity</li>
 *   <li>serviceCode references ServiceCatalog.code</li>
 *   <li>Cannot be negative</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "invoice_items",
        indexes = {
                @Index(name = "idx_ii_invoice", columnList = "invoice_id"),
                @Index(name = "idx_ii_service", columnList = "service_code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InvoiceItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The invoice this item belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * Service code from ServiceCatalog (e.g., "DOC-001").
     * Denormalized for performance — avoids JOIN at query time.
     */
    @Column(name = "service_code", nullable = false, length = 50)
    private String serviceCode;

    /**
     * Description of the service (snapshot at time of invoicing).
     */
    @Column(length = 500)
    private String description;

    /**
     * Quantity of the service.
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    /**
     * Price per unit.
     */
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total price for this line item.
     * Computed: unitPrice × quantity
     */
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Recalculates totalPrice based on quantity and unitPrice.
     */
    public void recalculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity != null ? quantity : 1));
    }

    /**
     * Validates that quantity is positive.
     *
     * @return true if quantity is greater than zero
     */
    public boolean isValidQuantity() {
        return quantity != null && quantity > 0;
    }

    /**
     * Validates that unitPrice is non-negative.
     *
     * @return true if unitPrice is zero or positive
     */
    public boolean isValidPrice() {
        return unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0;
    }
}
