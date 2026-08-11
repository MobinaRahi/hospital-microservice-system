package hospital.billingservice.model;

import hospital.billingservice.model.enums.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Catalog of all medical services offered by the hospital with their prices.
 * Used when creating invoices to reference service codes and prices.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Code must be unique across all services</li>
 *   <li>Price must be positive</li>
 *   <li>Inactive services cannot be added to new invoices</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "service_catalog",
        indexes = {
                @Index(name = "idx_sc_code", columnList = "code", unique = true),
                @Index(name = "idx_sc_category", columnList = "category"),
                @Index(name = "idx_sc_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ServiceCatalog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique service code (e.g., "DOC-001", "LAB-042").
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Human-readable name of the service.
     */
    @Column(nullable = false, length = 300)
    private String name;

    /**
     * Category of the service (visit, lab test, surgery, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;

    /**
     * Price of the service in local currency.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * Whether this service is currently available for invoicing.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Validates that the price is positive.
     *
     * @return true if price is greater than zero
     */
    public boolean isValidPrice() {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
}
