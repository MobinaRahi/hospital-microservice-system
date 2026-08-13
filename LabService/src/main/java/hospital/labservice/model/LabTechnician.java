package hospital.labservice.model;

import hospital.labservice.model.enums.LabShift;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a laboratory technician who performs tests and verifies results.
 * Contains personal information, employment details, and shift assignment.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each technician has a unique employeeCode</li>
 *   <li>Technicians must have a certification number</li>
 *   <li>Specialization indicates area of expertise</li>
 *   <li>Shift assignment determines work schedule</li>
 *   <li>Active flag indicates if technician is currently employed</li>
 * </ul>
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields like firstName, lastName, and employeeCode are stored locally
 * for performance reasons, even though they exist in AuthService.
 * This avoids frequent cross-service calls.</p>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_technicians",
        indexes = {
                @Index(name = "idx_lab_tech_user", columnList = "userId", unique = true),
                @Index(name = "idx_lab_tech_code", columnList = "employeeCode", unique = true),
                @Index(name = "idx_lab_tech_shift", columnList = "shift"),
                @Index(name = "idx_lab_tech_active", columnList = "isActive")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabTechnician extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID from AuthService.
     * Links technician to their authentication account.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * Technician's first name.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Technician's last name.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Unique employee code for identification.
     * Example: "TECH-001", "LAB-2026-001"
     */
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    /**
     * Technician's area of specialization.
     * Example: "Hematology", "Microbiology", "Biochemistry"
     */
    @Column(length = 100)
    private String specialization;

    /**
     * Certification number issued by relevant authority.
     */
    @Column(name = "certification_number", length = 50)
    private String certificationNumber;

    /**
     * Assigned work shift.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabShift shift;

    /**
     * Whether the technician is currently active and employed.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Date when the technician was hired.
     */
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets the full name of the technician.
     *
     * @return firstName + " " + lastName
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Checks if the technician is currently active.
     *
     * @return true if isActive is true
     */
    public boolean isActiveTechnician() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Deactivates the technician (marks as inactive).
     * Used when technician leaves or is suspended.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Activates the technician (marks as active).
     * Used when technician returns from leave or is reinstated.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Changes the technician's shift assignment.
     *
     * @param newShift the new shift to assign
     */
    public void changeShift(LabShift newShift) {
        this.shift = newShift;
    }

    /**
     * Updates the technician's specialization.
     *
     * @param newSpecialization the new specialization
     */
    public void updateSpecialization(String newSpecialization) {
        this.specialization = newSpecialization;
    }

    /**
     * Calculates years of service.
     *
     * @return number of complete years since hire date
     */
    public long getYearsOfService() {
        if (this.hireDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.YEARS.between(this.hireDate, LocalDate.now());
    }
}
