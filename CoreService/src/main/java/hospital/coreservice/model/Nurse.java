package hospital.coreservice.model;

import hospital.coreservice.model.enums.NursePosition;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a nurse in the hospital system.
 * <p>
 * This class stores professional and personal information about nurses,
 * including their department assignments, shift preferences, and years of experience.
 * Nurses can work in multiple departments and have preferred shifts.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "nurseEntity")
@Table(name = "nurses",
        indexes = {
                @Index(name = "idx_nurse_national_id", columnList = "national_id"),
                @Index(name = "idx_nurse_phone", columnList = "phone_number"),
                @Index(name = "idx_nurse_code", columnList = "nurse_code"),
                @Index(name = "idx_nurse_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Nurse {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Authentication Information ==========
    /**
     * User ID reference from Auth Service.
     * <p>Links this nurse to a system user account for login and permissions.</p>
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ========== Personal Information ==========
    /**
     * Nurse's first name.
     */
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    /**
     * Nurse's last name.
     */
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    /**
     * National ID (unique identifier for Iranian citizens).
     * <p>Stored as String to preserve leading zeros.</p>
     */
    @Column(name = "national_id", length = 10, nullable = false, unique = true)
    private String nationalId;

    // ========== Contact Information ==========
    /**
     * Mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    /**
     * Email address for professional communication.
     */
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    // ========== Professional Information ==========
    /**
     * Unique employee code for the nurse.
     * <p>Format: "NUR-XXXXX" where XXXXX is a sequential number.</p>
     */
    @Column(name = "nurse_code", length = 50, nullable = false, unique = true)
    private String nurseCode;

    /**
     * List of departments this nurse works in.
     * <p>A nurse can be assigned to multiple departments.</p>
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "nurse_department",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"),
            foreignKey = @ForeignKey(name = "fk_nurse_departments_nurse"),
            inverseForeignKey = @ForeignKey(name = "fk_nurse_departments_department"),
            indexes = {
                    @Index(name = "idx_nurse_dept_nurse", columnList = "nurse_id"),
                    @Index(name = "idx_nurse_dept_dept", columnList = "department_id")
            }
    )
    @Builder.Default
    private List<Department> departmentList = new ArrayList<>();

    /**
     * Nurse's position/rank (HEAD_NURSE, SENIOR_NURSE, STAFF_NURSE, etc.).
     */
    @Column(name = "nurse_position", nullable = false)
    @Enumerated(EnumType.STRING)
    private NursePosition position;

    /**
     * List of shift preferences for this nurse.
     * <p>Indicates which shifts (Morning, Evening, Night) the nurse prefers to work.</p>
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "nurse_shifts",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id"),
            foreignKey = @ForeignKey(name = "fk_nurse_shifts_nurse"),
            inverseForeignKey = @ForeignKey(name = "fk_nurse_shifts_shift"),
            indexes = {
                    @Index(name = "idx_nurse_shift_nurse", columnList = "nurse_id"),
                    @Index(name = "idx_nurse_shift_shift", columnList = "shift_id")
            }
    )
    @Builder.Default
    private List<Shift> shiftPreferenceList = new ArrayList<>();

    /**
     * Number of years of professional experience.
     */
    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    // ========== Status ==========
    /**
     * Active/Inactive status of the nurse.
     * <p>- true: Currently employed and active in the system</p>
     * <p>- false: No longer employed or on extended leave</p>
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // ========== Audit Fields ==========
    /**
     * Timestamp when this nurse record was created.
     * <p>Automatically populated by Hibernate.</p>
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========== Helper Methods ==========
    /**
     * Adds a department to this nurse's department list.
     *
     * @param department the department to add
     */
    public void addDepartment(Department department) {
        departmentList.add(department);
    }

    /**
     * Removes a department from this nurse's department list.
     *
     * @param department the department to remove
     */
    public void removeDepartment(Department department) {
        departmentList.remove(department);
    }

    /**
     * Adds a shift preference for this nurse.
     *
     * @param shift the shift to add
     */
    public void addShiftPreference(Shift shift) {
        shiftPreferenceList.add(shift);
    }

    /**
     * Removes a shift preference from this nurse.
     *
     * @param shift the shift to remove
     */
    public void removeShiftPreference(Shift shift) {
        shiftPreferenceList.remove(shift);
    }
}
