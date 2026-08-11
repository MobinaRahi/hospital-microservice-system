package hospital.billingservice.model;

import hospital.billingservice.model.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a monthly payroll record for an employee.
 *
 * <p><strong>Payroll Formula:</strong></p>
 * <pre>
 * netSalary = baseSalary + overtime + bonuses - deductions
 * </pre>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each employee can have only one payroll record per month/year</li>
 *   <li>Status workflow: PENDING → PROCESSED → PAID</li>
 *   <li>CANCELLED payroll records are not processed</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "payrolls",
        indexes = {
                @Index(name = "idx_payroll_employee", columnList = "employee_id"),
                @Index(name = "idx_payroll_month_year", columnList = "month,year"),
                @Index(name = "idx_payroll_status", columnList = "status"),
                @Index(name = "idx_payroll_unique", columnList = "employee_id,month,year", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payroll extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee this payroll record belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Month of the payroll (1-12).
     */
    @Column(nullable = false)
    private Integer month;

    /**
     * Year of the payroll (e.g., 2026).
     */
    @Column(nullable = false)
    private Integer year;

    /**
     * Base monthly salary.
     */
    @Column(name = "base_salary", nullable = false, precision = 12, scale = 2)
    private java.math.BigDecimal baseSalary;

    /**
     * Overtime pay amount.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private java.math.BigDecimal overtime = java.math.BigDecimal.ZERO;

    /**
     * Bonuses and incentives.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private java.math.BigDecimal bonuses = java.math.BigDecimal.ZERO;

    /**
     * Deductions (tax, insurance, penalties, etc.).
     */
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private java.math.BigDecimal deductions = java.math.BigDecimal.ZERO;

    /**
     * Net salary after all calculations.
     * Computed: baseSalary + overtime + bonuses - deductions
     */
    @Column(name = "net_salary", nullable = false, precision = 12, scale = 2)
    private java.math.BigDecimal netSalary;

    /**
     * Date the salary was paid.
     */
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    /**
     * Current status of the payroll record.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PayrollStatus status = PayrollStatus.PENDING;

    /**
     * Calculates and returns the net salary.
     *
     * @return baseSalary + overtime + bonuses - deductions
     */
    public java.math.BigDecimal calculateNetSalary() {
        return baseSalary
                .add(overtime != null ? overtime : java.math.BigDecimal.ZERO)
                .add(bonuses != null ? bonuses : java.math.BigDecimal.ZERO)
                .subtract(deductions != null ? deductions : java.math.BigDecimal.ZERO);
    }

    /**
     * Checks if this payroll can be cancelled.
     *
     * @return true if status is PENDING or PROCESSED (not already PAID or CANCELLED)
     */
    public boolean canBeCancelled() {
        return status == PayrollStatus.PENDING || status == PayrollStatus.PROCESSED;
    }
}
