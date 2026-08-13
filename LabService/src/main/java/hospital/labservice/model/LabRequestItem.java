package hospital.labservice.model;

import hospital.labservice.model.enums.RequestItemStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents an individual test item within a lab request.
 * Each item corresponds to a specific lab test to be performed.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *                      ↓
 *                  CANCELLED
 * </pre>
 *
 * <p><strong>Relationships:</strong></p>
 * <ul>
 *   <li>Many-to-One with LabRequest (parent request)</li>
 *   <li>Many-to-One with LabTest (test definition)</li>
 *   <li>One-to-One with LabResult (test result)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_request_items",
        indexes = {
                @Index(name = "idx_lab_req_item_request", columnList = "lab_request_id"),
                @Index(name = "idx_lab_req_item_test", columnList = "test_id"),
                @Index(name = "idx_lab_req_item_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabRequestItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parent lab request that this item belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_request_id", nullable = false)
    private LabRequest labRequest;

    /**
     * Lab test definition for this item.
     * Specifies what test to perform.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private LabTest test;

    /**
     * Display name of the test (copied from LabTest for quick access).
     */
    @Column(name = "test_name", nullable = false, length = 200)
    private String testName;

    /**
     * Current status of this test item.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestItemStatus status = RequestItemStatus.PENDING;

    /**
     * Lab result for this test item.
     * Populated after the test is completed.
     */
    @OneToOne(mappedBy = "requestItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private LabResult result;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Starts processing this test item.
     * Transitions status from PENDING to PROCESSING.
     *
     * @throws IllegalStateException if item is not in PENDING status
     */
    public void startProcessing() {
        if (this.status != RequestItemStatus.PENDING) {
            throw new IllegalStateException("Can only process PENDING items. Current status: " + this.status);
        }
        this.status = RequestItemStatus.PROCESSING;
    }

    /**
     * Completes this test item.
     * Transitions status from PROCESSING to COMPLETED.
     *
     * @throws IllegalStateException if item is not in PROCESSING status
     */
    public void complete() {
        if (this.status != RequestItemStatus.PROCESSING) {
            throw new IllegalStateException("Can only complete PROCESSING items. Current status: " + this.status);
        }
        this.status = RequestItemStatus.COMPLETED;
    }

    /**
     * Cancels this test item.
     * Can be cancelled from any status except COMPLETED.
     *
     * @throws IllegalStateException if item is already COMPLETED
     */
    public void cancel() {
        if (this.status == RequestItemStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel COMPLETED items");
        }
        this.status = RequestItemStatus.CANCELLED;
    }

    /**
     * Checks if the item is pending.
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return this.status == RequestItemStatus.PENDING;
    }

    /**
     * Checks if the item is being processed.
     *
     * @return true if status is PROCESSING
     */
    public boolean isProcessing() {
        return this.status == RequestItemStatus.PROCESSING;
    }

    /**
     * Checks if the item is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return this.status == RequestItemStatus.COMPLETED;
    }
}
