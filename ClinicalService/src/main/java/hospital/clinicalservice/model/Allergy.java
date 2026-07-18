package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a patient allergy (drug, food, environmental).
 * Critical for preventing dangerous drug prescriptions.
 *
 * @author Mobina
 */
@Entity(name = "allergyEntity")
@Table(name = "allergies",
        indexes = {
                @Index(name = "idx_allergy_patient", columnList = "patientId"),
                @Index(name = "idx_allergy_type", columnList = "type"),
                @Index(name = "idx_allergy_severity", columnList = "severity")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Allergy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Patient ID */
    @Column(nullable = false)
    private Long patientId;

    /** Allergy type */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergyType type;

    /** Allergen name */
    @Column(name = "allergen_name", length = 200, nullable = false)
    private String allergenName;

    @Column(name = "reaction",length = 500)
    private String reaction;

    /** Severity level */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergySeverity severity;

    /** Symptoms */
    @Column(name = "symptoms", length = 500)
    private String symptoms;

    /** Notes */
    @Column(name = "notes", length = 500)
    private String notes;

    /** Is active? */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
