package hospital.labservice.model;

import hospital.labservice.model.enums.SampleType;
import hospital.labservice.model.enums.TestCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a laboratory test available in the system.
 * Defines the test properties including category, sample type, pricing, and turnaround time.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each test must have a unique code</li>
 *   <li>Tests can be active or inactive</li>
 *   <li>Price is stored as BigDecimal for precision</li>
 *   <li>Turnaround hours indicates expected result delivery time</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li>CBC (Complete Blood Count) - HEMATOLOGY category, BLOOD sample</li>
 *   <li>Glucose - BIOCHEMISTRY category, BLOOD sample</li>
 *   <li>Urinalysis - URINALYSIS category, URINE sample</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_tests",
        indexes = {
                @Index(name = "idx_lab_test_code", columnList = "code", unique = true),
                @Index(name = "idx_lab_test_category", columnList = "category"),
                @Index(name = "idx_lab_test_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code identifier for the test.
     * Example: "CBC", "GLU", "URI"
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Display name of the test.
     * Example: "Complete Blood Count", "Fasting Blood Sugar"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Category of the test (HEMATOLOGY, BIOCHEMISTRY, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestCategory category;

    /**
     * LOINC code for standardization.
     * LOINC (Logical Observation Identifiers Names and Codes) is a universal code system.
     */
    @Column(name = "loinc_code", length = 20)
    private String loincCode;

    /**
     * Normal range for the test result.
     * Example: "70-100 mg/dL", "4.5-5.5 x10^6/uL"
     */
    @Column(name = "normal_range", length = 100)
    private String normalRange;

    /**
     * Unit of measurement for the result.
     * Example: "mg/dL", "x10^6/uL", "%"
     */
    @Column(length = 50)
    private String unit;

    /**
     * Price of the test in local currency.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Instructions for patient preparation before the test.
     * Example: "Fast for 8 hours", "No special preparation required"
     */
    @Column(name = "preparation_instructions", length = 500)
    private String preparationInstructions;

    /**
     * Whether this test requires a sample to be collected.
     * Some tests (like consultations) may not require physical samples.
     */
    @Column(name = "requires_sample", nullable = false)
    @Builder.Default
    private Boolean requiresSample = true;

    /**
     * Type of sample required (BLOOD, URINE, etc.).
     * Only applicable if requiresSample is true.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type")
    private SampleType sampleType;

    /**
     * Expected turnaround time in hours for result delivery.
     * Example: 24 for next-day results, 2 for 2-hour results
     */
    @Column(name = "turnaround_hours", nullable = false)
    private Integer turnaroundHours;

    /**
     * Whether this test is currently active and available for ordering.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Checks if the test requires a sample.
     *
     * @return true if sample collection is required
     */
    public boolean needsSample() {
        return Boolean.TRUE.equals(requiresSample);
    }

    /**
     * Checks if the test is currently active.
     *
     * @return true if the test is available for ordering
     */
    public boolean isTestActive() {
        return Boolean.TRUE.equals(isActive);
    }
}
