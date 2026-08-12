package hospital.billingservice.service;

import hospital.billingservice.dto.employee.EmployeeCreateDto;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.dto.employee.EmployeeUpdateDto;
import hospital.billingservice.model.enums.EmployeePosition;

import java.util.List;

/**
 * Service interface for Employee.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Employee code must be unique</li>
 *   <li>userId references AuthService user account</li>
 *   <li>Soft delete is supported (deactivate employee)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface EmployeeService {

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ══════════════════════════════════════════════════════════════════

    /**
     * Creates a new employee.
     *
     * @param dto the employee creation data
     * @return the created employee
     */
    EmployeeResponseDto createEmployee(EmployeeCreateDto dto);

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Gets an employee by their ID.
     *
     * @param id the employee ID
     * @return the employee
     */
    EmployeeResponseDto getEmployeeById(Long id);

    /**
     * Gets an employee by their unique employee code.
     *
     * @param employeeCode the employee code
     * @return the employee
     */
    EmployeeResponseDto getEmployeeByCode(String employeeCode);

    /**
     * Gets an employee by their user ID (from AuthService).
     *
     * @param userId the user ID
     * @return the employee
     */
    EmployeeResponseDto getEmployeeByUserId(Long userId);

    /**
     * Gets all active employees.
     *
     * @return list of active employees
     */
    List<EmployeeResponseDto> getAllActiveEmployees();

    /**
     * Gets all employees (including inactive).
     *
     * @return list of all employees
     */
    List<EmployeeResponseDto> getAllEmployees();

    /**
     * Gets employees by position.
     *
     * @param position the employee position
     * @return list of employees with the position
     */
    List<EmployeeResponseDto> getEmployeesByPosition(EmployeePosition position);

    /**
     * Gets active employees by department.
     *
     * @param department the department name
     * @return list of active employees in the department
     */
    List<EmployeeResponseDto> getEmployeesByDepartment(String department);

    /**
     * Gets employees by position and active status.
     *
     * @param position the employee position
     * @return list of active employees with the position
     */
    List<EmployeeResponseDto> getActiveEmployeesByPosition(EmployeePosition position);

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing employee.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the employee ID
     * @param dto the update data
     * @return the updated employee
     */
    EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateDto dto);

    /**
     * Toggles the active status of an employee.
     *
     * @param id the employee ID
     * @return the updated employee
     */
    EmployeeResponseDto toggleActive(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes (deactivates) an employee.
     *
     * @param id the employee ID
     */
    void deleteEmployee(Long id);

    // ══════════════════════════════════════════════════════════════════
    // Statistics
    // ══════════════════════════════════════════════════════════════════

    /**
     * Counts all active employees.
     *
     * @return number of active employees
     */
    long countActiveEmployees();

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if an employee code is already in use.
     *
     * @param employeeCode the code to check
     * @return true if the code exists
     */
    boolean codeExists(String employeeCode);
}
