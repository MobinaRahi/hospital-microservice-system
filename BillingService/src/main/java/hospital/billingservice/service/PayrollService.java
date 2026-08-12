package hospital.billingservice.service;

import hospital.billingservice.dto.payroll.PayrollCreateDto;
import hospital.billingservice.dto.payroll.PayrollResponseDto;
import hospital.billingservice.dto.payroll.PayrollUpdateDto;
import hospital.billingservice.model.enums.PayrollStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Payroll.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each employee can have only one payroll record per month/year</li>
 *   <li>Status workflow: PENDING → PROCESSED → PAID</li>
 *   <li>CANCELLED payroll records are not processed</li>
 *   <li>Net salary = baseSalary + overtime + bonuses - deductions</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PayrollService {

    // ══════════════════════════════════════════════════════════════════
    // Create
    // ═════════════════════════════════════════════════════════════════

    /**
     * Creates a new payroll record.
     * <p>Automatically calculates net salary.</p>
     *
     * @param dto the payroll creation data
     * @return the created payroll record
     */
    PayrollResponseDto createPayroll(PayrollCreateDto dto);

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    /**
     * Gets a payroll record by its ID.
     *
     * @param id the payroll ID
     * @return the payroll record
     */
    PayrollResponseDto getPayrollById(Long id);

    /**
     * Gets all payroll records for a specific employee.
     *
     * @param employeeId the employee ID
     * @return list of payroll records for the employee
     */
    List<PayrollResponseDto> getPayrollsByEmployee(Long employeeId);

    /**
     * Gets a specific payroll record for an employee in a given month/year.
     *
     * @param employeeId the employee ID
     * @param month      the month (1-12)
     * @param year       the year
     * @return the payroll record
     */
    PayrollResponseDto getPayrollByEmployeeAndPeriod(Long employeeId, Integer month, Integer year);

    /**
     * Gets all payroll records for a specific month and year.
     *
     * @param month the month (1-12)
     * @param year  the year
     * @return list of payroll records
     */
    List<PayrollResponseDto> getPayrollsByPeriod(Integer month, Integer year);

    /**
     * Gets payroll records by status.
     *
     * @param status the payroll status
     * @return list of payroll records with the status
     */
    List<PayrollResponseDto> getPayrollsByStatus(PayrollStatus status);

    /**
     * Gets pending payroll records ordered by employee ID.
     *
     * @return list of pending payroll records
     */
    List<PayrollResponseDto> getPendingPayrolls();

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing payroll record.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the payroll ID
     * @param dto the update data
     * @return the updated payroll record
     */
    PayrollResponseDto updatePayroll(Long id, PayrollUpdateDto dto);

    /**
     * Calculates and returns the net salary for a payroll record.
     *
     * @param id the payroll ID
     * @return the calculated net salary
     */
    BigDecimal calculateNetSalary(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Status Transitions
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Processes a pending payroll record.
     * Status transition: PENDING → PROCESSED
     *
     * @param id the payroll ID
     * @return the updated payroll record
     */
    PayrollResponseDto processPayroll(Long id);

    /**
     * Marks a processed payroll as paid.
     * Status transition: PROCESSED → PAID
     *
     * @param id the payroll ID
     * @return the updated payroll record
     */
    PayrollResponseDto markAsPaid(Long id);

    /**
     * Cancels a payroll record.
     * Can only cancel PENDING or PROCESSED records.
     *
     * @param id the payroll ID
     * @return the updated payroll record
     */
    PayrollResponseDto cancelPayroll(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a payroll record.
     *
     * @param id the payroll ID
     */
    void deletePayroll(Long id);

    // ══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if a payroll record already exists for an employee in a given month/year.
     *
     * @param employeeId the employee ID
     * @param month      the month
     * @param year       the year
     * @return true if exists
     */
    boolean existsForEmployeeInPeriod(Long employeeId, Integer month, Integer year);
}
