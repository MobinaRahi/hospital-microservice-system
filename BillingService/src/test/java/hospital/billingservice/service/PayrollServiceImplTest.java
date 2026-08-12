package hospital.billingservice.service;

import hospital.billingservice.dto.payroll.PayrollCreateDto;
import hospital.billingservice.dto.payroll.PayrollResponseDto;
import hospital.billingservice.exception.payroll.DuplicatePayrollException;
import hospital.billingservice.exception.payroll.IllegalPayrollStatusException;
import hospital.billingservice.exception.payroll.PayrollNotFoundException;
import hospital.billingservice.mapper.PayrollMapper;
import hospital.billingservice.model.Payroll;
import hospital.billingservice.model.enums.PayrollStatus;
import hospital.billingservice.repository.PayrollRepository;
import hospital.billingservice.service.impl.PayrollServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PayrollServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class PayrollServiceImplTest {

    @Mock private PayrollRepository payrollRepository;
    @Mock private PayrollMapper payrollMapper;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    private Payroll testPayroll;

    @BeforeEach
    void setUp() {
        testPayroll = Payroll.builder()
                .id(1L)
                .month(8)
                .year(2026)
                .baseSalary(BigDecimal.valueOf(15000000))
                .overtime(BigDecimal.valueOf(2000000))
                .bonuses(BigDecimal.valueOf(1000000))
                .deductions(BigDecimal.valueOf(1500000))
                .netSalary(BigDecimal.valueOf(16500000))
                .status(PayrollStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Payroll")
    class CreatePayrollTests {

        @Test
        @DisplayName("should create payroll successfully")
        void shouldCreatePayroll() {
            PayrollCreateDto dto = PayrollCreateDto.builder()
                    .employeeId(100L).month(8).year(2026).baseSalary(BigDecimal.valueOf(15000000)).build();

            when(payrollRepository.existsByEmployeeIdAndMonthAndYear(100L, 8, 2026)).thenReturn(false);
            when(payrollMapper.toEntity(any(PayrollCreateDto.class))).thenReturn(testPayroll);
            when(payrollMapper.toResponseDto(any(Payroll.class)))
                    .thenReturn(PayrollResponseDto.builder().id(1L).build());
            when(payrollRepository.save(any(Payroll.class))).thenReturn(testPayroll);

            PayrollResponseDto result = payrollService.createPayroll(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(payrollRepository).save(argThat(p -> p.getStatus() == PayrollStatus.PENDING));
        }

        @Test
        @DisplayName("should throw when payroll exists for period")
        void shouldThrowWhenExistsForPeriod() {
            PayrollCreateDto dto = PayrollCreateDto.builder().employeeId(100L).month(8).year(2026).build();

            when(payrollRepository.existsByEmployeeIdAndMonthAndYear(100L, 8, 2026)).thenReturn(true);

            assertThatThrownBy(() -> payrollService.createPayroll(dto))
                    .isInstanceOf(DuplicatePayrollException.class);
        }
    }

    @Nested
    @DisplayName("Read Payroll")
    class ReadPayrollTests {

        @Test
        @DisplayName("should get payroll by id")
        void shouldGetById() {
            PayrollResponseDto expected = PayrollResponseDto.builder().id(1L).build();

            when(payrollRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayroll));
            when(payrollMapper.toResponseDto(testPayroll)).thenReturn(expected);

            PayrollResponseDto result = payrollService.getPayrollById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(payrollRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> payrollService.getPayrollById(999L))
                    .isInstanceOf(PayrollNotFoundException.class);
        }

        @Test
        @DisplayName("should get payrolls by status")
        void shouldGetByStatus() {
            when(payrollRepository.findByStatus(PayrollStatus.PENDING)).thenReturn(List.of(testPayroll));
            when(payrollMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    PayrollResponseDto.builder().id(1L).build()));

            assertThat(payrollService.getPayrollsByStatus(PayrollStatus.PENDING)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should process payroll from PENDING to PROCESSED")
        void shouldProcessPayroll() {
            PayrollResponseDto expected = PayrollResponseDto.builder().build();

            when(payrollRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayroll));
            when(payrollMapper.toResponseDto(any(Payroll.class))).thenReturn(expected);
            when(payrollRepository.save(any(Payroll.class))).thenReturn(testPayroll);

            payrollService.processPayroll(1L);

            verify(payrollRepository).save(argThat(p -> p.getStatus() == PayrollStatus.PROCESSED));
        }

        @Test
        @DisplayName("should throw when processing non-PENDING payroll")
        void shouldThrowWhenProcessingNonPending() {
            testPayroll.setStatus(PayrollStatus.PROCESSED);

            when(payrollRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayroll));

            assertThatThrownBy(() -> payrollService.processPayroll(1L))
                    .isInstanceOf(IllegalPayrollStatusException.class);
        }

        @Test
        @DisplayName("should mark payroll as paid")
        void shouldMarkAsPaid() {
            testPayroll.setStatus(PayrollStatus.PROCESSED);
            PayrollResponseDto expected = PayrollResponseDto.builder().build();

            when(payrollRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayroll));
            when(payrollMapper.toResponseDto(any(Payroll.class))).thenReturn(expected);
            when(payrollRepository.save(any(Payroll.class))).thenReturn(testPayroll);

            payrollService.markAsPaid(1L);

            verify(payrollRepository).save(argThat(p -> p.getStatus() == PayrollStatus.PAID));
        }
    }

    @Nested
    @DisplayName("Delete Payroll")
    class DeletePayrollTests {

        @Test
        @DisplayName("should soft delete payroll")
        void shouldSoftDelete() {
            when(payrollRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPayroll));

            payrollService.deletePayroll(1L);

            verify(payrollRepository).save(argThat(Payroll::isDeleted));
        }
    }
}
