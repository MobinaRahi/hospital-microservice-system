package hospital.adminservice.model;

import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a hospital bed that can be assigned to patients.
 * Tracks bed status, type, and current patient assignment.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each bed must have a unique bedNumber within the hospital</li>
 *   <li>Beds can be of different types (ICU, VIP, General, etc.)</li>
 *   <li>Bed status tracks availability and current state</li>
 *   <li>When a patient is assigned, status changes to OCCUPIED</li>
 *   <li>When patient is discharged, status returns to AVAILABLE</li>
 * </ul>
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * AVAILABLE → OCCUPIED (when patient assigned)
 * OCCUPIED → AVAILABLE (when patient discharged)
 * AVAILABLE → RESERVED (when reserved for future admission)
 * AVAILABLE → MAINTENANCE (when under maintenance)
 * AVAILABLE → CLEANING (when being cleaned)
 * </pre>
 *
 * <p><strong>Multi-Tenancy:</strong></p>
 * <ul>
 *   <li>Inherits tenantId from BaseEntity</li>
 *   <li>Each bed belongs to a specific hospital tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "beds",
        indexes = {
                @Index(name = "idx_bed_number", columnList = "bed_number"),
                @Index(name = "idx_bed_dept", columnList = "department_id"),
                @Index(name = "idx_bed_type", columnList = "type"),
                @Index(name = "idx_bed_status", columnList = "status"),
                @Index(name = "idx_bed_patient", columnList = "current_patient_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Bed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique bed number within the hospital.
     * Example: "ICU-001", "VIP-101", "GEN-205"
     */
    @Column(name = "bed_number", nullable = false, length = 50)
    private String bedNumber;

    /**
     * Department ID from CoreService.
     * Links bed to a specific department.
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * Type of bed (GENERAL, ICU, VIP, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedType type;

    /**
     * Current status of the bed.
     * Default: AVAILABLE
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BedStatus status = BedStatus.AVAILABLE;

    /**
     * Patient ID from CoreService (when bed is occupied).
     * Null when bed is available.
     */
    @Column(name = "current_patient_id")
    private Long currentPatientId;

    /**
     * Admission ID from CoreService (when bed is occupied).
     * Links to the current admission record.
     */
    @Column(name = "current_admission_id")
    private Long currentAdmissionId;

    /**
     * When the bed was assigned to the current patient.
     */
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    /**
     * Expected discharge date for the current patient.
     * Used for capacity planning.
     */
    @Column(name = "expected_discharge_date")
    private LocalDateTime expectedDischargeDate;

    /**
     * Additional notes about the bed.
     * Example: "Requires special equipment", "Near nurse station"
     */
    @Column(length = 500)
    private String notes;

    /**
     * Checks if the bed is currently available.
     *
     * @return true if status is AVAILABLE
     */
    public boolean isAvailable() {
        return status == BedStatus.AVAILABLE;
    }

    /**
     * Assigns the bed to a patient.
     * Changes status to OCCUPIED and records assignment time.
     *
     * @param patientId  the patient ID from CoreService
     * @param admissionId the admission ID from CoreService
     */
    public void assignToPatient(Long patientId, Long admissionId) {
        this.currentPatientId = patientId;
        this.currentAdmissionId = admissionId;
        this.status = BedStatus.OCCUPIED;
        this.assignedAt = LocalDateTime.now();
    }

    /**
     * Discharges the patient from the bed.
     * Resets all patient-related fields and sets status to AVAILABLE.
     */
    public void dischargePatient() {
        this.currentPatientId = null;
        this.currentAdmissionId = null;
        this.status = BedStatus.AVAILABLE;
        this.assignedAt = null;
        this.expectedDischargeDate = null;
    }
}
