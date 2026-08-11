package hospital.billingservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an insurance company or plan partnered with the hospital.
 * Manages coverage percentages, deductibles, and maximum coverage limits.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Code must be unique across all insurance plans</li>
 *   <li>coveragePercent is between 0 and 100</li>
 *   <li>hasDeductible indicates if the plan has a deductible</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "insurance_management",
        indexes = {
                @Index(name = "idx_insurance_code", columnList = "code", unique = true),
                @Index(name = "idx_insurance_name", columnList = "name"),
                @Index(name = "idx_insurance_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InsuranceManagement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the insurance company/plan.
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Unique code for this insurance plan.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Coverage percentage (0-100).
     */
    @Column(name = "coverage_percent", nullable = false)
    @Builder.Default
    private Integer coveragePercent = 0;

    /**
     * Whether this plan has a deductible.
     */
    @Column(name = "has_deductible", nullable = false)
    @Builder.Default
    private Boolean hasDeductible = false;

    /**
     * Deductible amount (if hasDeductible is true).
     */
    @Column(name = "deductible_amount")
    private Integer deductibleAmount;

    /**
     * Maximum coverage amount per year.
     */
    @Column(name = "max_coverage_per_year")
    private Integer maxCoveragePerYear;

    /**
     * Contact phone number.
     */
    @Column(length = 20)
    private String phone;

    /**
     * Contact email address.
     */
    @Column(length = 200)
    private String email;

    /**
     * Whether this insurance plan is currently active.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Patient insurance records linked to this plan.
     */
    @OneToMany(mappedBy = "insurance", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PatientInsurance> patientInsurances = new ArrayList<>();

    /**
     * Validates that coveragePercent is within valid range.
     *
     * @return true if coveragePercent is between 0 and 100
     */
    public boolean isValidCoveragePercent() {
        return coveragePercent >= 0 && coveragePercent <= 100;
    }
}
