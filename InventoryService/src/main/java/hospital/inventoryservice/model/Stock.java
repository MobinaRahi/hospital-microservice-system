package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a stock batch of a specific drug.
 * Tracks quantity, expiry date, storage location, and stock levels.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each drug can have multiple batches (different expiry dates/locations)</li>
 *   <li>Low stock alerts are triggered when quantity falls below minStockLevel</li>
 *   <li>Expired stock should not be dispensed</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "stocks",
        indexes = {
                @Index(name = "idx_stock_drug", columnList = "drug_id"),
                @Index(name = "idx_stock_expiry", columnList = "expiry_date"),
                @Index(name = "idx_stock_batch", columnList = "batch_number")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The drug this stock belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    /**
     * Batch/lot number from the manufacturer.
     */
    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    /**
     * Current quantity in stock.
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    /**
     * Minimum stock level — triggers reorder alert when quantity falls below this.
     */
    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    /**
     * Maximum stock level — for capacity planning.
     */
    @Column(name = "max_stock_level")
    private Integer maxStockLevel;

    /**
     * Physical storage location (e.g., "Warehouse A, Shelf 3").
     */
    @Column(length = 200)
    private String location;

    /**
     * Expiry date of this batch.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * When this stock was last restocked.
     */
    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    /**
     * ID of the user who last restocked this batch.
     */
    @Column(name = "last_restocked_by")
    private Long lastRestockedBy;

    /**
     * Checks whether this stock is expired.
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Checks whether this stock is running low.
     */
    public boolean isLowStock() {
        return minStockLevel != null && quantity <= minStockLevel;
    }
}
