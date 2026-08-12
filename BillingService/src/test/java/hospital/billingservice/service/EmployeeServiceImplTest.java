package hospital.billingservice.service;

import hospital.billingservice.dto.employee.EmployeeCreateDto;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.exception.employee.DuplicateEmployeeCodeException;
import hospital.billingservice.exception.employee.EmployeeNotFoundException;
import hospital.billingservice.mapper.EmployeeMapper;
import hospital.billingservice.model.Employee;
import hospital.billingservice.model.enums.EmployeePosition;
import hospital.billingservice.repository.EmployeeRepository;
import hospital.billingservice.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmployeeServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .id(1L)
                .employeeCode("EMP-001")
                .firstName("Ali")
                .lastName("Mohammadi")
                .position(EmployeePosition.ACCOUNTANT)
                .baseSalary(15000000)
                .hireDate(LocalDate.of(2020, 1, 1))
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Employee")
    class CreateEmployeeTests {

        @Test
        @DisplayName("should create employee successfully")
        void shouldCreateEmployee() {
            EmployeeCreateDto dto = EmployeeCreateDto.builder()
                    .employeeCode("EMP-001").firstName("Ali").lastName("Mohammadi")
                    .position(EmployeePosition.ACCOUNTANT).baseSalary(BigDecimal.valueOf(15000000))
                    .hireDate(LocalDate.of(2020, 1, 1)).build();

            when(employeeRepository.existsByEmployeeCode("EMP-001")).thenReturn(false);
            when(employeeMapper.toEntity(any(EmployeeCreateDto.class))).thenReturn(testEmployee);
            when(employeeMapper.toResponseDto(any(Employee.class)))
                    .thenReturn(EmployeeResponseDto.builder().id(1L).build());
            when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

            EmployeeResponseDto result = employeeService.createEmployee(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(employeeRepository).save(argThat(e -> e.getIsActive()));
        }

        @Test
        @DisplayName("should throw when code exists")
        void shouldThrowWhenCodeExists() {
            EmployeeCreateDto dto = EmployeeCreateDto.builder().employeeCode("EMP-001").build();

            when(employeeRepository.existsByEmployeeCode("EMP-001")).thenReturn(true);

            assertThatThrownBy(() -> employeeService.createEmployee(dto))
                    .isInstanceOf(DuplicateEmployeeCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read Employee")
    class ReadEmployeeTests {

        @Test
        @DisplayName("should get employee by id")
        void shouldGetById() {
            EmployeeResponseDto expected = EmployeeResponseDto.builder().id(1L).build();

            when(employeeRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployee));
            when(employeeMapper.toResponseDto(testEmployee)).thenReturn(expected);

            EmployeeResponseDto result = employeeService.getEmployeeById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(employeeRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeService.getEmployeeById(999L))
                    .isInstanceOf(EmployeeNotFoundException.class);
        }

        @Test
        @DisplayName("should get active employees")
        void shouldGetActiveEmployees() {
            when(employeeRepository.findByIsActiveTrue()).thenReturn(List.of(testEmployee));
            when(employeeMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    EmployeeResponseDto.builder().id(1L).build()));

            assertThat(employeeService.getAllActiveEmployees()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Employee")
    class UpdateEmployeeTests {

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            testEmployee.setIsActive(true);
            EmployeeResponseDto expected = EmployeeResponseDto.builder().build();

            when(employeeRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployee));
            when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(expected);
            when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

            employeeService.toggleActive(1L);

            verify(employeeRepository).save(argThat(e -> !e.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Delete Employee")
    class DeleteEmployeeTests {

        @Test
        @DisplayName("should soft delete employee")
        void shouldSoftDelete() {
            when(employeeRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployee));

            employeeService.deleteEmployee(1L);

            verify(employeeRepository).save(argThat(Employee::isDeleted));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count active employees")
        void shouldCountActiveEmployees() {
            when(employeeRepository.countByIsActiveTrue()).thenReturn(5L);

            assertThat(employeeService.countActiveEmployees()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(employeeRepository.existsByEmployeeCode("EMP-001")).thenReturn(true);

            assertThat(employeeService.codeExists("EMP-001")).isTrue();
        }
    }
}
