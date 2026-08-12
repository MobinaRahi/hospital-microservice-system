package hospital.billingservice.repository;

import hospital.billingservice.model.Payroll;
import hospital.billingservice.model.enums.PayrollStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Payroll entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface PayrollRepository extends BaseEntityRepository<Payroll, Long> {

    /**
     * Finds all payroll records for an employee.
     *
     * @param employeeId the employee ID
     * @return list of payroll records
     */
    List<Payroll> findByEmployeeId(Long employeeId);

    /**
     * Finds a specific payroll record for an employee in a given month/year.
     *
     * @param employeeId the employee ID
     * @param month      the month (1-12)
     * @param year       the year
     * @return payroll record if found
     */
    Optional<Payroll> findByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);

    /**
     * Finds all payroll records for a specific month and year.
     *
     * @param month the month (1-12)
     * @param year  the year
     * @return list of payroll records
     */
    List<Payroll> findByMonthAndYear(Integer month, Integer year);

    /**
     * Finds payroll records by status.
     *
     * @param status the payroll status
     * @return list of payroll records with the status
     */
    List<Payroll> findByStatus(PayrollStatus status);

    /**
     * Finds pending payroll records that need processing.
     *
     * @return list of pending payroll records
     */
    List<Payroll> findByStatusOrderByEmployeeIdAsc(PayrollStatus status);

    /**
     * Checks if a payroll record already exists for an employee in a given month/year.
     *
     * @param employeeId the employee ID
     * @param month      the month
     * @param year       the year
     * @return true if exists
     */
    boolean existsByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);

    /**
     * Counts all payroll records with a specific status.
     *
     * @param status the payroll status
     * @return number of payroll records
     */
    long countByStatus(PayrollStatus status);
}
