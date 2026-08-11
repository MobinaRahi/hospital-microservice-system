package hospital.billingservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a patient's insurance record, linking a patient to an insurance plan.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>A patient can have multiple insurance plans, but only one primary</li>
 *   <li>policyNumber must be unique per patient-insurance pair</li>
 *   <li>expiryDate is tracked to prevent claims on expired insurance</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "patient_insurance",
        indexes = {
                @Index(name = "idx_pi_patient", columnList = "patient_id"),
                @Index(name = "idx_pi_insurance", columnList = "insurance_id"),
                @Index(name = "idx_pi_policy", columnList = "policy_number"),
                @Index(name = "idx_pi_primary", columnList = "is_primary")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PatientInsurance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Patient ID from CoreService.
     * Patient is not a local entity — referenced by ID only.
     */
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    /**
     * The insurance plan this patient is enrolled in.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", nullable = false)
    private InsuranceManagement insurance;

    /**
     * Policy number issued by the insurance company.
     */
    @Column(name = "policy_number", nullable = false, length = 100)
    private String policyNumber;

    /**
     * Expiry date of this insurance policy.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * Whether this is the primary insurance for the patient.
     * A patient can have multiple insurances but only one primary.
     */
    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;

    /**
     * Checks whether this insurance policy is currently valid.
     *
     * @return true if expiryDate is null or in the future
     */
    public boolean isValid() {
        return expiryDate == null || !expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Checks whether this policy is the primary insurance.
     *
     * @return true if isPrimary is true
     */
    public boolean isPrimaryInsurance() {
        return isPrimary != null && isPrimary;
    }
}
