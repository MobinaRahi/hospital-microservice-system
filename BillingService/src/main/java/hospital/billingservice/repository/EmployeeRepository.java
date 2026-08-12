package hospital.billingservice.repository;

import hospital.billingservice.model.Employee;
import hospital.billingservice.model.enums.EmployeePosition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface EmployeeRepository extends BaseEntityRepository<Employee, Long> {

    /**
     * Finds an employee by their unique employee code.
     *
     * @param employeeCode the employee code
     * @return employee if found
     */
    Optional<Employee> findByEmployeeCode(String employeeCode);

    /**
     * Finds an employee by their user ID (from AuthService).
     *
     * @param userId the user ID
     * @return employee if found
     */
    Optional<Employee> findByUserId(Long userId);

    /**
     * Finds all active employees.
     *
     * @return list of active employees
     */
    List<Employee> findByIsActiveTrue();

    /**
     * Finds employees by position.
     *
     * @param position the employee position
     * @return list of employees with the position
     */
    List<Employee> findByPosition(EmployeePosition position);

    /**
     * Finds active employees by department.
     *
     * @param department the department name
     * @return list of active employees in the department
     */
    List<Employee> findByDepartmentContainingIgnoreCaseAndIsActiveTrue(String department);

    /**
     * Checks if an employee code already exists.
     *
     * @param employeeCode the code to check
     * @return true if exists
     */
    boolean existsByEmployeeCode(String employeeCode);

    /**
     * Finds employees by position and active status.
     *
     * @param position the employee position
     * @return list of active employees with the position
     */
    List<Employee> findByPositionAndIsActiveTrue(EmployeePosition position);

    /**
     * Counts all active employees.
     *
     * @return number of active employees
     */
    long countByIsActiveTrue();
}
