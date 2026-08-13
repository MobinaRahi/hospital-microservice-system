package hospital.labservice.model;

import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a biological sample collected from a patient for laboratory testing.
 * Tracks sample collection, quality assessment, and receipt at the lab.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each sample has a unique sampleNumber</li>
 *   <li>Sample quality is assessed upon receipt (GOOD, HEMOLYZED, etc.)</li>
 *   <li>Collection and receipt times are tracked for turnaround calculation</li>
 *   <li>Container type specifies the collection vessel (tube, cup, etc.)</li>
 * </ul>
 *
 * <p><strong>Sample Quality Flags:</strong></p>
 * <ul>
 *   <li>GOOD - Sample is suitable for testing</li>
 *   <li>HEMOLYZED - Red blood cells have ruptured (affects certain tests)</li>
 *   <li>LIPEMIC - High lipid content (affects certain tests)</li>
 *   <li>CLOTTED - Sample has clotted (affects certain tests)</li>
 *   <li>INSUFFICIENT - Not enough sample volume</li>
 *   <li>CONTAMINATED - Sample is contaminated</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "samples",
        indexes = {
                @Index(name = "idx_sample_number", columnList = "sampleNumber", unique = true),
                @Index(name = "idx_sample_request", columnList = "lab_request_id"),
                @Index(name = "idx_sample_type", columnList = "sampleType"),
                @Index(name = "idx_sample_quality", columnList = "quality"),
                @Index(name = "idx_sample_collection", columnList = "collectionDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parent lab request that this sample belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_request_id", nullable = false)
    private LabRequest labRequest;

    /**
     * Unique sample number for tracking.
     * Example: "SMP-2026-000456"
     */
    @Column(name = "sample_number", nullable = false, unique = true, length = 50)
    private String sampleNumber;

    /**
     * Type of biological sample (BLOOD, URINE, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type", nullable = false)
    private SampleType sampleType;

    /**
     * Date and time when the sample was collected from the patient.
     */
    @Column(name = "collection_date", nullable = false)
    private LocalDateTime collectionDate;

    /**
     * User ID who collected the sample (from AuthService).
     */
    @Column(name = "collected_by")
    private Long collectedBy;

    /**
     * Location where the sample was collected.
     * Example: "Room 201", "Phlebotomy Station 3"
     */
    @Column(name = "collection_site", length = 100)
    private String collectionSite;

    /**
     * Type of container used for sample collection.
     * Example: "EDTA tube", "Urine cup", "Blood culture bottle"
     */
    @Column(name = "container_type", length = 100)
    private String containerType;

    /**
     * Quality assessment of the sample upon receipt.
     * Determines if the sample is suitable for testing.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SampleQuality quality = SampleQuality.GOOD;

    /**
     * Date and time when the sample was received at the laboratory.
     */
    @Column(name = "received_at_lab")
    private LocalDateTime receivedAtLab;

    /**
     * User ID who received the sample at the lab (from AuthService).
     */
    @Column(name = "received_by")
    private Long receivedBy;

    /**
     * Additional notes about the sample.
     * Example: "Slightly hemolyzed but acceptable for chemistry tests"
     */
    @Column(length = 500)
    private String notes;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Marks the sample as received at the laboratory.
     * Sets receivedAtLab timestamp and receivedBy user.
     *
     * @param receivedBy the ID of the user receiving the sample
     */
    public void markReceived(Long receivedBy) {
        this.receivedAtLab = LocalDateTime.now();
        this.receivedBy = receivedBy;
    }

    /**
     * Checks if the sample has been received at the lab.
     *
     * @return true if receivedAtLab is not null
     */
    public boolean isReceived() {
        return this.receivedAtLab != null;
    }

    /**
     * Checks if the sample quality is good and suitable for testing.
     *
     * @return true if quality is GOOD
     */
    public boolean isGoodQuality() {
        return this.quality == SampleQuality.GOOD;
    }

    /**
     * Checks if the sample has quality issues that may affect test results.
     *
     * @return true if quality is not GOOD
     */
    public boolean hasQualityIssues() {
        return this.quality != SampleQuality.GOOD;
    }

    /**
     * Calculates the time elapsed between collection and receipt at the lab.
     *
     * @return time elapsed in hours, or null if not yet received
     */
    public Long getTransportTimeHours() {
        if (this.receivedAtLab == null || this.collectionDate == null) {
            return null;
        }
        return java.time.Duration.between(this.collectionDate, this.receivedAtLab).toHours();
    }
}
