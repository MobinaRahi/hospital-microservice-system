package hospital.billingservice.service.impl;

import hospital.billingservice.dto.employee.EmployeeCreateDto;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.dto.employee.EmployeeUpdateDto;
import hospital.billingservice.exception.employee.DuplicateEmployeeCodeException;
import hospital.billingservice.exception.employee.EmployeeNotFoundException;
import hospital.billingservice.mapper.EmployeeMapper;
import hospital.billingservice.model.Employee;
import hospital.billingservice.model.enums.EmployeePosition;
import hospital.billingservice.repository.EmployeeRepository;
import hospital.billingservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link EmployeeService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto dto) {
        log.info("Creating employee: {}", dto.getEmployeeCode());

        // Validate unique employee code
        if (employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new DuplicateEmployeeCodeException(dto.getEmployeeCode());
        }

        // Map DTO to entity
        Employee employee = employeeMapper.toEntity(dto);
        employee.setIsActive(true);

        // Save and return
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created with id: {}", saved.getId());

        return employeeMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.debug("Fetching employee by id: {}", id);

        Employee employee = employeeRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeNotFoundException.byId(id));

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByCode(String employeeCode) {
        log.debug("Fetching employee by code: {}", employeeCode);

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> EmployeeNotFoundException.byCode(employeeCode));

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByUserId(Long userId) {
        log.debug("Fetching employee by userId: {}", userId);

        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> EmployeeNotFoundException.byUserId(userId));

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllActiveEmployees() {
        log.debug("Fetching all active employees");

        List<Employee> employees = employeeRepository.findByIsActiveTrue();
        return employeeMapper.toResponseDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        log.debug("Fetching all employees");

        List<Employee> employees = employeeRepository.findAllNotDeleted();
        return employeeMapper.toResponseDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByPosition(EmployeePosition position) {
        log.debug("Fetching employees by position: {}", position);

        List<Employee> employees = employeeRepository.findByPosition(position);
        return employeeMapper.toResponseDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByDepartment(String department) {
        log.debug("Fetching employees by department: {}", department);

        List<Employee> employees = employeeRepository.findByDepartmentContainingIgnoreCaseAndIsActiveTrue(department);
        return employeeMapper.toResponseDtoList(employees);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getActiveEmployeesByPosition(EmployeePosition position) {
        log.debug("Fetching active employees by position: {}", position);

        List<Employee> employees = employeeRepository.findByPositionAndIsActiveTrue(position);
        return employeeMapper.toResponseDtoList(employees);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateDto dto) {
        log.info("Updating employee id: {}", id);

        Employee employee = employeeRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeNotFoundException.byId(id));

        // Map update DTO to entity
        employeeMapper.updateEntity(dto, employee);

        Employee saved = employeeRepository.save(employee);
        log.info("Employee updated id: {}", saved.getId());

        return employeeMapper.toResponseDto(saved);
    }

    @Override
    public EmployeeResponseDto toggleActive(Long id) {
        log.info("Toggling active status for employee id: {}", id);

        Employee employee = employeeRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeNotFoundException.byId(id));

        employee.setIsActive(!employee.getIsActive());
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deleteEmployee(Long id) {
        log.info("Soft-deleting employee id: {}", id);

        Employee employee = employeeRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeNotFoundException.byId(id));

        employee.softDelete(null);
        employeeRepository.save(employee);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Statistics
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public long countActiveEmployees() {
        return employeeRepository.countByIsActiveTrue();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String employeeCode) {
        return employeeRepository.existsByEmployeeCode(employeeCode);
    }
}
