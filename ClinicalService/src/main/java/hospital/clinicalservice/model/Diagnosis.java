package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a medical diagnosis using ICD-10 standard codes.
 * Each diagnosis is linked to an encounter and can be primary or secondary.
 *
 * <p><strong>ICD-10 Examples:</strong></p>
 * <ul>
 *   <li>I10 = Essential (primary) hypertension</li>
 *   <li>E11 = Type 2 diabetes mellitus</li>
 *   <li>J45 = Asthma</li>
 * </ul>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each encounter can have multiple diagnoses</li>
 *   <li>Only ONE diagnosis can be marked as primary</li>
 *   <li>ICD-10 code is required for insurance and billing</li>
 * </ul>
 *
 * @author Mobina
 */
@Entity(name = "diagnosisEntity")
@Table(name = "diagnoses",
        indexes = {
                @Index(name = "idx_diagnosis_encounter", columnList = "encounter_id"),
                @Index(name = "idx_diagnosis_icd", columnList = "icd10Code")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Diagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The encounter (visit) this diagnosis belongs to.
     * Many diagnoses can belong to one encounter.
     * LAZY loading: encounter data is loaded only when accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    /**
     * ICD-10 code (International Classification of Diseases, 10th Revision).
     * Standard international code for disease classification.
     * Required for insurance claims and medical records.
     * Example: "I10" for hypertension, "E11" for diabetes.
     */
    @Column(name = "icd10_code", length = 10, nullable = false)
    private String icd10Code;

    /**
     * Human-readable name of the diagnosis.
     * Example: "Essential hypertension", "Type 2 diabetes mellitus"
     * Max 200 characters.
     */
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * Detailed description of the diagnosis.
     * Additional clinical information, severity, stage, etc.
     * Max 1000 characters.
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Whether this is the primary diagnosis for the encounter.
     * true = Primary diagnosis (main reason for visit)
     * false = Secondary diagnosis (additional conditions)
     * Only ONE diagnosis per encounter should be primary.
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /**
     * Additional notes from the doctor about this diagnosis.
     * Treatment plan, follow-up instructions, etc.
     * Max 1000 characters.
     */
    @Column(name = "notes", length = 1000)
    private String notes;
}
