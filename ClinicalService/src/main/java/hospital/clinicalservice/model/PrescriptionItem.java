package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a single drug item within a prescription.
 * Links to InventoryService via drugId for drug details.
 *
 * @author Mobina
 */
@Entity(name = "prescriptionItemEntity")
@Table(name = "prescription_items",
        indexes = {
                @Index(name = "idx_presc_item_prescription", columnList = "prescription_id")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class PrescriptionItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** نسخه مربوطه */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    /** Drug ID (from InventoryService) */
    @Column(name = "drug_id")
    private Long drugId;

    /** Drug name */
    @Column(name = "drug_name", length = 200, nullable = false)
    private String drugName;

    /** دوز (مثلاً 500mg) */
    @Column(name = "dosage", length = 50, nullable = false)
    private String dosage;

    /** Frequency */
    @Column(name = "frequency", length = 50, nullable = false)
    private String frequency;

    /** مدت مصرف (مثلاً 7 روز) */
    @Column(name = "duration", length = 50)
    private String duration;

    /** Instructions */
    @Column(name = "instructions", length = 500)
    private String instructions;

    /** Quantity */
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "substitution_allowed",nullable = false)
    private boolean substitutionAllowed;
}
