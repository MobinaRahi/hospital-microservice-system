package hospital.labservice.model;

import hospital.labservice.model.enums.ResultFlag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents the result of a laboratory test.
 * Contains the measured value, normal range, flag indicating if value is within normal range,
 * and verification information.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each result is linked to a specific LabRequestItem</li>
 *   <li>Results must be verified by a qualified technician</li>
 *   <li>Result flags indicate if values are within normal range</li>
 *   <li>Critical values require immediate notification</li>
 * </ul>
 *
 * <p><strong>Result Flags:</strong></p>
 * <ul>
 *   <li>NORMAL - Value is within normal range</li>
 *   <li>LOW - Value is below normal range</li>
 *   <li>HIGH - Value is above normal range</li>
 *   <li>CRITICAL_LOW - Value is dangerously below normal range</li>
 *   <li>CRITICAL_HIGH - Value is dangerously above normal range</li>
 *   <li>ABNORMAL - Value is not within normal range (general)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_results",
        indexes = {
                @Index(name = "idx_lab_result_item", columnList = "lab_request_item_id", unique = true),
                @Index(name = "idx_lab_result_flag", columnList = "flag"),
                @Index(name = "idx_lab_result_performed", columnList = "performedAt"),
                @Index(name = "idx_lab_result_verified", columnList = "verifiedAt")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Lab request item that this result belongs to.
     * One-to-One relationship with LabRequestItem.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_request_item_id", nullable = false, unique = true)
    private LabRequestItem requestItem;

    /**
     * Measured value of the test result.
     * Example: "142", "7.2", "Positive"
     */
    @Column(nullable = false, length = 100)
    private String value;

    /**
     * Normal reference range for this test.
     * Example: "70-100 mg/dL", "4.5-5.5 x10^6/uL"
     */
    @Column(name = "normal_range", length = 100)
    private String normalRange;

    /**
     * Flag indicating if the result is within normal range.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ResultFlag flag = ResultFlag.NORMAL;

    /**
     * Unit of measurement for the result.
     * Example: "mg/dL", "x10^6/uL", "%"
     */
    @Column(length = 50)
    private String unit;

    /**
     * Date and time when the test was performed.
     */
    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    /**
     * User ID of the technician who performed the test (from AuthService).
     */
    @Column(name = "performed_by")
    private Long performedBy;

    /**
     * Date and time when the result was verified.
     */
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    /**
     * User ID of the technician who verified the result (from AuthService).
     */
    @Column(name = "verified_by")
    private Long verifiedBy;

    /**
     * Additional notes about the result.
     * Example: "Sample was slightly hemolyzed, may affect potassium results"
     */
    @Column(length = 500)
    private String notes;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Verifies the result.
     * Sets verifiedAt timestamp and verifiedBy user.
     *
     * @param verifiedBy the ID of the user verifying the result
     */
    public void verify(Long verifiedBy) {
        this.verifiedAt = LocalDateTime.now();
        this.verifiedBy = verifiedBy;
    }

    /**
     * Checks if the result has been verified.
     *
     * @return true if verifiedAt is not null
     */
    public boolean isVerified() {
        return this.verifiedAt != null;
    }

    /**
     * Checks if the result is within normal range.
     *
     * @return true if flag is NORMAL
     */
    public boolean isNormal() {
        return this.flag == ResultFlag.NORMAL;
    }

    /**
     * Checks if the result is critical (CRITICAL_LOW or CRITICAL_HIGH).
     * Critical results require immediate notification.
     *
     * @return true if flag is CRITICAL_LOW or CRITICAL_HIGH
     */
    public boolean isCritical() {
        return this.flag == ResultFlag.CRITICAL_LOW || this.flag == ResultFlag.CRITICAL_HIGH;
    }

    /**
     * Checks if the result is abnormal (not NORMAL).
     *
     * @return true if flag is not NORMAL
     */
    public boolean isAbnormal() {
        return this.flag != ResultFlag.NORMAL;
    }

}
