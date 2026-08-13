package hospital.adminservice.repository;

import hospital.adminservice.model.EmployeeShift;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for EmployeeShift entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByEmployeeIdAndDate - Employee shift on specific date</li>
 *   <li>findByShiftIdAndDate - All employees on a shift</li>
 *   <li>findByDateRange - Shifts in date range</li>
 *   <li>findPresentEmployees - Employees who worked</li>
 *   <li>findAbsentEmployees - Employees who didn't work</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface EmployeeShiftRepository extends BaseEntityRepository<EmployeeShift, Long> {

    /**
     * Finds employee shift assignment on a specific date.
     *
     * @param employeeId the employee ID
     * @param date       the date
     * @return employee shift if found
     */
    Optional<EmployeeShift> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    /**
     * Finds all employee shift assignments for a shift on a date.
     *
     * @param shiftId the shift ID
     * @param date    the date
     * @return list of employee shifts
     */
    List<EmployeeShift> findByShiftIdAndDate(Long shiftId, LocalDate date);

    /**
     * Finds all shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return list of employee shifts
     */
    List<EmployeeShift> findByEmployeeId(Long employeeId);

    /**
     * Finds shifts in a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of shifts in range
     */
    @Query("SELECT es FROM EmployeeShift es WHERE es.date BETWEEN :startDate AND :endDate AND es.deleted = false")
    List<EmployeeShift> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Finds employees who were present on a shift.
     *
     * @param shiftId the shift ID
     * @param date    the date
     * @return list of present employee shifts
     */
    List<EmployeeShift> findByShiftIdAndDateAndIsPresentTrue(Long shiftId, LocalDate date);

    /**
     * Finds employees who were absent on a shift.
     *
     * @param shiftId the shift ID
     * @param date    the date
     * @return list of absent employee shifts
     */
    List<EmployeeShift> findByShiftIdAndDateAndIsPresentFalse(Long shiftId, LocalDate date);

    /**
     * Counts total shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return total shift count
     */
    @Query("SELECT COUNT(es) FROM EmployeeShift es WHERE es.employeeId = :employeeId AND es.deleted = false")
    long countByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * Counts present shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return present shift count
     */
    @Query("SELECT COUNT(es) FROM EmployeeShift es WHERE es.employeeId = :employeeId AND es.isPresent = true AND es.deleted = false")
    long countPresentByEmployeeId(@Param("employeeId") Long employeeId);
}
