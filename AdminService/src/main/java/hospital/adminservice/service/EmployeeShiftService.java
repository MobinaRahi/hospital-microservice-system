package hospital.adminservice.service;

import hospital.adminservice.dto.employeeshift.EmployeeShiftCreateDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftResponseDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftUpdateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for EmployeeShift management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each employee can be assigned to one shift per day</li>
 *   <li>Actual start/end times are recorded when employee clocks in/out</li>
 *   <li>isPresent flag tracks whether employee actually worked the shift</li>
 *   <li>Used for payroll calculation and attendance tracking</li>
 *   <li>Soft delete supported</li>
 * </ul>
 *
 * <p><strong>Workflow:</strong></p>
 * <ol>
 *   <li>Shift is scheduled for employee (isPresent=false)</li>
 *   <li>Employee clocks in (actualStart recorded)</li>
 *   <li>Employee clocks out (actualEnd recorded)</li>
 *   <li>isPresent set to true</li>
 * </ol>
 *
 * @author MobinaRahi
 */
public interface EmployeeShiftService {

    /**
     * Creates a new employee shift assignment.
     *
     * @param dto the employee shift creation data
     * @return the created employee shift
     */
    EmployeeShiftResponseDto createEmployeeShift(EmployeeShiftCreateDto dto);

    /**
     * Gets an employee shift by its ID.
     *
     * @param id the employee shift ID
     * @return the employee shift
     */
    EmployeeShiftResponseDto getEmployeeShiftById(Long id);

    /**
     * Gets employee shift assignment on a specific date.
     *
     * @param employeeId the employee ID
     * @param date       the date
     * @return the employee shift
     */
    EmployeeShiftResponseDto getEmployeeShiftByDate(Long employeeId, LocalDate date);

    /**
     * Gets all shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return list of employee shifts
     */
    List<EmployeeShiftResponseDto> getShiftsByEmployee(Long employeeId);

    /**
     * Gets all employee shifts on a specific date for a shift.
     *
     * @param shiftId the shift ID
     * @param date    the date
     * @return list of employee shifts
     */
    List<EmployeeShiftResponseDto> getShiftsByDate(Long shiftId, LocalDate date);

    /**
     * Gets employee shifts in a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of shifts in range
     */
    List<EmployeeShiftResponseDto> getShiftsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Marks an employee as present and records actual times.
     *
     * @param id    the employee shift ID
     * @param start clock-in time
     * @param end   clock-out time
     * @return the updated employee shift
     */
    EmployeeShiftResponseDto markPresent(Long id, LocalDateTime start, LocalDateTime end);

    /**
     * Marks an employee as absent for a shift.
     *
     * @param id the employee shift ID
     * @return the updated employee shift
     */
    EmployeeShiftResponseDto markAbsent(Long id);

    /**
     * Updates an employee shift assignment.
     *
     * @param id  the employee shift ID
     * @param dto the update data
     * @return the updated employee shift
     */
    EmployeeShiftResponseDto updateEmployeeShift(Long id, EmployeeShiftUpdateDto dto);

    /**
     * Soft-deletes an employee shift assignment.
     *
     * @param id the employee shift ID
     */
    void deleteEmployeeShift(Long id);

    /**
     * Counts total shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return total shift count
     */
    long countByEmployee(Long employeeId);

    /**
     * Counts present shifts for an employee.
     *
     * @param employeeId the employee ID
     * @return present shift count
     */
    long countPresentByEmployee(Long employeeId);
}
