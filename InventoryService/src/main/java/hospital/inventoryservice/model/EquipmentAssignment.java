package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents the assignment of equipment to a patient or department.
 * Tracks who has the equipment and for how long.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>An equipment can be assigned to a patient OR a department (not both)</li>
 *   <li>Return date is null while equipment is in use</li>
 *   <li>Only AVAILABLE equipment can be assigned</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "equipment_assignments",
        indexes = {
                @Index(name = "idx_ea_equipment", columnList = "equipment_id"),
                @Index(name = "idx_ea_patient", columnList = "patient_id"),
                @Index(name = "idx_ea_department", columnList = "department_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EquipmentAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The equipment being assigned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * Patient ID from CoreService (nullable — assignment may be to a department instead).
     */
    @Column(name = "patient_id")
    private Long patientId;

    /**
     * Department ID from CoreService (nullable — assignment may be to a patient instead).
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * When the equipment was assigned.
     */
    @Column(name = "assigned_date", nullable = false)
    private LocalDateTime assignedDate;

    /**
     * Expected return date (can be null for indefinite assignments).
     */
    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    /**
     * Actual return date (null while still in use).
     */
    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    /**
     * ID of the user who assigned the equipment.
     */
    @Column(name = "assigned_by")
    private Long assignedBy;

    /**
     * Notes about the assignment (e.g., condition notes).
     */
    @Column(length = 500)
    private String notes;

    /**
     * Checks whether this assignment is still active (equipment not returned).
     */
    public boolean isActive() {
        return actualReturnDate == null;
    }
}
