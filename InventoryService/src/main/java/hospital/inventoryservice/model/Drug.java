package hospital.inventoryservice.model;

import hospital.inventoryservice.model.enums.DrugForm;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pharmaceutical drug in the hospital inventory.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each drug must belong to a category</li>
 *   <li>Drugs requiring prescription are flagged</li>
 *   <li>Barcode must be unique across all drugs</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "drugs",
        indexes = {
                @Index(name = "idx_drug_category", columnList = "category_id"),
                @Index(name = "idx_drug_form", columnList = "form"),
                @Index(name = "idx_drug_name", columnList = "generic_name"),
                @Index(name = "idx_drug_barcode", columnList = "barcode", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Drug extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Generic (scientific) name of the drug.
     */
    @Column(name = "generic_name", nullable = false, length = 200)
    private String genericName;

    /**
     * Brand/trade name of the drug.
     */
    @Column(name = "brand_name", length = 200)
    private String brandName;

    /**
     * Drug strength (e.g., "500mg", "10ml").
     */
    @Column(length = 50)
    private String strength;

    /**
     * Pharmaceutical form (tablet, capsule, syrup, ...).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrugForm form;

    /**
     * Category of the drug.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private DrugCategory category;

    /**
     * Price per unit in local currency.
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * Whether this drug requires a doctor's prescription.
     */
    @Column(name = "requires_prescription")
    @Builder.Default
    private Boolean requiresPrescription = false;

    /**
     * Whether this drug is currently active/available.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Unique barcode for the drug.
     */
    @Column(unique = true, length = 50)
    private String barcode;

    /**
     * Description and usage instructions.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Stock records for this drug (inverse side).
     */
    @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();
}
