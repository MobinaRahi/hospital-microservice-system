package hospital.labservice.model;

import hospital.labservice.model.enums.RequestPriority;
import hospital.labservice.model.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a laboratory test request submitted by a doctor.
 * Contains patient information, clinical notes, priority level, and status workflow.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → APPROVED → SAMPLE_COLLECTED → IN_PROGRESS → COMPLETED
 *                                      ↓
 *                                  CANCELLED
 *                                  REJECTED
 * </pre>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each request has a unique requestNumber</li>
 *   <li>Requests can have multiple items (LabRequestItem)</li>
 *   <li>Priority determines processing order (STAT > URGENT > ROUTINE)</li>
 *   <li>Clinical notes provide context for the lab technician</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_requests",
        indexes = {
                @Index(name = "idx_lab_req_number", columnList = "requestNumber", unique = true),
                @Index(name = "idx_lab_req_patient", columnList = "patientId"),
                @Index(name = "idx_lab_req_doctor", columnList = "doctorId"),
                @Index(name = "idx_lab_req_status", columnList = "status"),
                @Index(name = "idx_lab_req_priority", columnList = "priority"),
                @Index(name = "idx_lab_req_date", columnList = "requestDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique request number for tracking.
     * Example: "LAB-2026-000123"
     */
    @Column(name = "request_number", nullable = false, unique = true, length = 50)
    private String requestNumber;

    /**
     * Patient ID from CoreService.
     * Links request to a specific patient.
     */
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    /**
     * Doctor ID from CoreService who ordered the tests.
     */
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    /**
     * Encounter ID from ClinicalService (optional).
     * Links request to a specific patient encounter.
     */
    @Column(name = "encounter_id")
    private Long encounterId;

    /**
     * Date and time when the request was submitted.
     */
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    /**
     * Priority level of the request.
     * STAT requests are processed immediately.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestPriority priority = RequestPriority.ROUTINE;

    /**
     * Current status of the request.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    /**
     * Clinical notes providing context for the lab technician.
     * Example: "Patient has diabetes, check HbA1c"
     */
    @Column(name = "clinical_notes", length = 1000)
    private String clinicalNotes;

    /**
     * User ID who requested the tests (from AuthService).
     */
    @Column(name = "requested_by")
    private Long requestedBy;

    /**
     * User ID who approved the request (from AuthService).
     */
    @Column(name = "approved_by")
    private Long approvedBy;

    /**
     * Date and time when the request was approved.
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    /**
     * List of test items in this request.
     * Each item represents a specific test to be performed.
     */
    @OneToMany(mappedBy = "labRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LabRequestItem> items = new ArrayList<>();

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Approves the request.
     * Transitions status from PENDING to APPROVED.
     *
     * @param approverId the ID of the user approving the request
     * @throws IllegalStateException if request is not in PENDING status
     */
    public void approve(Long approverId) {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Can only approve PENDING requests. Current status: " + this.status);
        }
        this.status = RequestStatus.APPROVED;
        this.approvedBy = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * Rejects the request.
     * Transitions status from PENDING to REJECTED.
     *
     * @throws IllegalStateException if request is not in PENDING status
     */
    public void reject() {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Can only reject PENDING requests. Current status: " + this.status);
        }
        this.status = RequestStatus.REJECTED;
    }

    /**
     * Marks sample as collected.
     * Transitions status from APPROVED to SAMPLE_COLLECTED.
     *
     * @throws IllegalStateException if request is not in APPROVED status
     */
    public void markSampleCollected() {
        if (this.status != RequestStatus.APPROVED) {
            throw new IllegalStateException("Can only collect sample for APPROVED requests. Current status: " + this.status);
        }
        this.status = RequestStatus.SAMPLE_COLLECTED;
    }

    /**
     * Starts processing the request.
     * Transitions status from SAMPLE_COLLECTED to IN_PROGRESS.
     *
     * @throws IllegalStateException if request is not in SAMPLE_COLLECTED status
     */
    public void startProcessing() {
        if (this.status != RequestStatus.SAMPLE_COLLECTED) {
            throw new IllegalStateException("Can only process SAMPLE_COLLECTED requests. Current status: " + this.status);
        }
        this.status = RequestStatus.IN_PROGRESS;
    }

    /**
     * Completes the request.
     * Transitions status from IN_PROGRESS to COMPLETED.
     *
     * @throws IllegalStateException if request is not in IN_PROGRESS status
     */
    public void complete() {
        if (this.status != RequestStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete IN_PROGRESS requests. Current status: " + this.status);
        }
        this.status = RequestStatus.COMPLETED;
    }

    /**
     * Cancels the request.
     * Can be cancelled from any status except COMPLETED.
     *
     * @throws IllegalStateException if request is already COMPLETED
     */
    public void cancel() {
        if (this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel COMPLETED requests");
        }
        this.status = RequestStatus.CANCELLED;
    }

    /**
     * Checks if the request is pending approval.
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return this.status == RequestStatus.PENDING;
    }

    /**
     * Checks if the request is approved and ready for sample collection.
     *
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return this.status == RequestStatus.APPROVED;
    }

    /**
     * Checks if the request is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return this.status == RequestStatus.COMPLETED;
    }

    /**
     * Checks if the request is urgent (STAT or URGENT priority).
     *
     * @return true if priority is STAT or URGENT
     */
    public boolean isUrgent() {
        return this.priority == RequestPriority.STAT || this.priority == RequestPriority.URGENT;
    }

    /**
     * Adds a test item to the request.
     *
     * @param item the lab request item to add
     */
    public void addItem(LabRequestItem item) {
        items.add(item);
        item.setLabRequest(this);
    }

    /**
     * Removes a test item from the request.
     *
     * @param item the lab request item to remove
     */
    public void removeItem(LabRequestItem item) {
        items.remove(item);
        item.setLabRequest(null);
    }
}
