package hospital.billingservice.service.impl;

import hospital.billingservice.dto.payroll.PayrollCreateDto;
import hospital.billingservice.dto.payroll.PayrollResponseDto;
import hospital.billingservice.dto.payroll.PayrollUpdateDto;
import hospital.billingservice.exception.payroll.DuplicatePayrollException;
import hospital.billingservice.exception.payroll.IllegalPayrollStatusException;
import hospital.billingservice.exception.payroll.PayrollNotFoundException;
import hospital.billingservice.mapper.PayrollMapper;
import hospital.billingservice.model.Payroll;
import hospital.billingservice.model.enums.PayrollStatus;
import hospital.billingservice.repository.PayrollRepository;
import hospital.billingservice.service.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link PayrollService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollMapper payrollMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PayrollResponseDto createPayroll(PayrollCreateDto dto) {
        log.info("Creating payroll for employee: {} for {}/{}", dto.getEmployeeId(), dto.getMonth(), dto.getYear());

        // Validate unique payroll per employee/month/year
        if (payrollRepository.existsByEmployeeIdAndMonthAndYear(dto.getEmployeeId(), dto.getMonth(), dto.getYear())) {
            throw new DuplicatePayrollException(dto.getEmployeeId(), dto.getMonth(), dto.getYear());
        }

        // Map DTO to entity
        Payroll payroll = payrollMapper.toEntity(dto);
        payroll.setStatus(PayrollStatus.PENDING);
        payroll.setNetSalary(payroll.calculateNetSalary());

        // Save and return
        Payroll saved = payrollRepository.save(payroll);
        log.info("Payroll created with id: {}", saved.getId());

        return payrollMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PayrollResponseDto getPayrollById(Long id) {
        log.debug("Fetching payroll by id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        return payrollMapper.toResponseDto(payroll);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDto> getPayrollsByEmployee(Long employeeId) {
        log.debug("Fetching payrolls for employee: {}", employeeId);

        List<Payroll> payrolls = payrollRepository.findByEmployeeId(employeeId);
        return payrollMapper.toResponseDtoList(payrolls);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollResponseDto getPayrollByEmployeeAndPeriod(Long employeeId, Integer month, Integer year) {
        log.debug("Fetching payroll for employee: {} for {}/{}", employeeId, month, year);

        Payroll payroll = payrollRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> PayrollNotFoundException.byPeriod(employeeId, month, year));

        return payrollMapper.toResponseDto(payroll);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDto> getPayrollsByPeriod(Integer month, Integer year) {
        log.debug("Fetching payrolls for {}/{}", month, year);

        List<Payroll> payrolls = payrollRepository.findByMonthAndYear(month, year);
        return payrollMapper.toResponseDtoList(payrolls);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDto> getPayrollsByStatus(PayrollStatus status) {
        log.debug("Fetching payrolls by status: {}", status);

        List<Payroll> payrolls = payrollRepository.findByStatus(status);
        return payrollMapper.toResponseDtoList(payrolls);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDto> getPendingPayrolls() {
        log.debug("Fetching pending payrolls");

        List<Payroll> payrolls = payrollRepository.findByStatusOrderByEmployeeIdAsc(PayrollStatus.PENDING);
        return payrollMapper.toResponseDtoList(payrolls);
    }

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PayrollResponseDto updatePayroll(Long id, PayrollUpdateDto dto) {
        log.info("Updating payroll id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        // Map update DTO to entity
        payrollMapper.updateEntity(dto, payroll);

        Payroll saved = payrollRepository.save(payroll);
        log.info("Payroll updated id: {}", saved.getId());

        return payrollMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateNetSalary(Long id) {
        log.debug("Calculating net salary for payroll id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        return payroll.calculateNetSalary();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Status Transitions
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PayrollResponseDto processPayroll(Long id) {
        log.info("Processing payroll id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        if (payroll.getStatus() != PayrollStatus.PENDING) {
            throw IllegalPayrollStatusException.cannotProcess(payroll.getStatus().name());
        }

        payroll.setStatus(PayrollStatus.PROCESSED);
        payroll.setNetSalary(payroll.calculateNetSalary());
        Payroll saved = payrollRepository.save(payroll);

        return payrollMapper.toResponseDto(saved);
    }

    @Override
    public PayrollResponseDto markAsPaid(Long id) {
        log.info("Marking payroll as paid id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        if (payroll.getStatus() != PayrollStatus.PROCESSED) {
            throw IllegalPayrollStatusException.cannotMarkPaid(payroll.getStatus().name());
        }

        payroll.setStatus(PayrollStatus.PAID);
        payroll.setPaymentDate(LocalDate.now());
        Payroll saved = payrollRepository.save(payroll);

        return payrollMapper.toResponseDto(saved);
    }

    @Override
    public PayrollResponseDto cancelPayroll(Long id) {
        log.info("Cancelling payroll id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        if (!payroll.canBeCancelled()) {
            throw IllegalPayrollStatusException.cannotCancel(payroll.getStatus().name());
        }

        payroll.setStatus(PayrollStatus.CANCELLED);
        Payroll saved = payrollRepository.save(payroll);

        return payrollMapper.toResponseDto(saved);
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deletePayroll(Long id) {
        log.info("Soft-deleting payroll id: {}", id);

        Payroll payroll = payrollRepository.findNotDeletedById(id)
                .orElseThrow(() -> PayrollNotFoundException.byId(id));

        payroll.softDelete(null);
        payrollRepository.save(payroll);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean existsForEmployeeInPeriod(Long employeeId, Integer month, Integer year) {
        return payrollRepository.existsByEmployeeIdAndMonthAndYear(employeeId, month, year);
    }
}
