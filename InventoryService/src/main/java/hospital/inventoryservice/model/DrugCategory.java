package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hierarchical drug category.
 * Supports multi-level categorization via parentId self-reference.
 *
 * <p><strong>Example hierarchy:</strong></p>
 * <pre>
 * Medications (level=1)
 *   └── Antibiotics (level=2, parent=Medications)
 *         └── Penicillins (level=3, parent=Antibiotics)
 * </pre>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "drug_categories",
        indexes = {
                @Index(name = "idx_drug_category_parent", columnList = "parent_id"),
                @Index(name = "idx_drug_category_level", columnList = "level"),
                @Index(name = "idx_drug_category_name", columnList = "name")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DrugCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category.
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Unique code for the category (e.g., "ANT-001").
     */
    @Column(length = 50)
    private String code;

    /**
     * Description of the category.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Parent category ID for hierarchical structure.
     * Null for root categories.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DrugCategory parent;

    /**
     * Level in the hierarchy (1 = root, 2 = child, 3 = grandchild, ...).
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer level = 1;

    /**
     * Whether this category is active.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Child categories (inverse side of the relationship).
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DrugCategory> children = new ArrayList<>();

    /**
     * Drugs in this category (inverse side).
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Drug> drugs = new ArrayList<>();
}
