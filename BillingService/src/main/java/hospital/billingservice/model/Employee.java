package hospital.billingservice.model;

import hospital.billingservice.model.enums.EmployeePosition;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a non-medical hospital employee (administrative, support, etc.).
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>employeeCode must be unique</li>
 *   <li>userId references the AuthService user account</li>
 *   <li>baseSalary is used as the basis for payroll calculations</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "employees",
        indexes = {
                @Index(name = "idx_emp_code", columnList = "employee_code", unique = true),
                @Index(name = "idx_emp_user", columnList = "user_id"),
                @Index(name = "idx_emp_position", columnList = "position"),
                @Index(name = "idx_emp_department", columnList = "department"),
                @Index(name = "idx_emp_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID from AuthService.
     * Links employee to their authentication account.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Unique employee code (e.g., "EMP-001").
     */
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    /**
     * Employee's first name.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Employee's last name.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Position/role of the employee.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeePosition position;

    /**
     * Department where the employee works.
     */
    @Column(length = 200)
    private String department;

    /**
     * Date the employee was hired.
     */
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    /**
     * Base monthly salary in local currency.
     */
    @Column(name = "base_salary", nullable = false, precision = 12, scale = 2)
    private Integer baseSalary;

    /**
     * Bank account number for salary deposit.
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    /**
     * Whether this employee is currently active.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Payroll records for this employee.
     */
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payroll> payrolls = new ArrayList<>();

    /**
     * Gets the full name of the employee.
     *
     * @return firstName + " " + lastName
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculates years of service as of today.
     *
     * @return number of complete years since hireDate
     */
    public long getYearsOfService() {
        if (hireDate == null) return 0;
        return java.time.temporal.ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }
}
