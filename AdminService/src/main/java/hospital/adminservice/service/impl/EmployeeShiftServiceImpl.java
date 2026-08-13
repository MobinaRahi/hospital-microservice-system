package hospital.adminservice.service.impl;

import hospital.adminservice.dto.employeeshift.EmployeeShiftCreateDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftResponseDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftUpdateDto;
import hospital.adminservice.exception.employeeshift.AlreadyMarkedException;
import hospital.adminservice.exception.employeeshift.DuplicateEmployeeShiftException;
import hospital.adminservice.exception.employeeshift.EmployeeShiftNotFoundException;
import hospital.adminservice.mapper.EmployeeShiftMapper;
import hospital.adminservice.model.EmployeeShift;
import hospital.adminservice.model.Shift;
import hospital.adminservice.repository.EmployeeShiftRepository;
import hospital.adminservice.repository.ShiftRepository;
import hospital.adminservice.service.EmployeeShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeShiftServiceImpl implements EmployeeShiftService {

    private final EmployeeShiftRepository employeeShiftRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeShiftMapper employeeShiftMapper;

    @Override
    public EmployeeShiftResponseDto createEmployeeShift(EmployeeShiftCreateDto dto) {
        log.info("Creating employee shift for employee {} on {}", dto.getEmployeeId(), dto.getDate());

        // Check if employee already has a shift on this date
        if (employeeShiftRepository.findByEmployeeIdAndDate(dto.getEmployeeId(), dto.getDate()).isPresent()) {
            throw new DuplicateEmployeeShiftException(dto.getEmployeeId(), dto.getDate().toString());
        }

        // Get shift definition
        Shift shift = shiftRepository.findNotDeletedById(dto.getShiftId())
                .orElseThrow(() -> hospital.adminservice.exception.shift.ShiftNotFoundException.byId(dto.getShiftId()));

        EmployeeShift employeeShift = employeeShiftMapper.toEntity(dto);
        employeeShift.setShift(shift);

        EmployeeShift saved = employeeShiftRepository.save(employeeShift);
        log.info("Employee shift created with id: {}", saved.getId());

        return employeeShiftMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeShiftResponseDto getEmployeeShiftById(Long id) {
        log.debug("Fetching employee shift by id: {}", id);

        EmployeeShift employeeShift = employeeShiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(id));

        return employeeShiftMapper.toResponseDto(employeeShift);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeShiftResponseDto getEmployeeShiftByDate(Long employeeId, LocalDate date) {
        log.debug("Fetching employee shift for employee {} on {}", employeeId, date);

        EmployeeShift employeeShift = employeeShiftRepository.findByEmployeeIdAndDate(employeeId, date)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(employeeId));

        return employeeShiftMapper.toResponseDto(employeeShift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeShiftResponseDto> getShiftsByEmployee(Long employeeId) {
        log.debug("Fetching shifts for employee: {}", employeeId);

        List<EmployeeShift> shifts = employeeShiftRepository.findByEmployeeId(employeeId);
        return employeeShiftMapper.toResponseDtoList(shifts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeShiftResponseDto> getShiftsByDate(Long shiftId, LocalDate date) {
        log.debug("Fetching shifts for shift {} on {}", shiftId, date);

        List<EmployeeShift> shifts = employeeShiftRepository.findByShiftIdAndDate(shiftId, date);
        return employeeShiftMapper.toResponseDtoList(shifts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeShiftResponseDto> getShiftsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching shifts from {} to {}", startDate, endDate);

        List<EmployeeShift> shifts = employeeShiftRepository.findByDateRange(startDate, endDate);
        return employeeShiftMapper.toResponseDtoList(shifts);
    }

    @Override
    public EmployeeShiftResponseDto markPresent(Long id, LocalDateTime start, LocalDateTime end) {
        log.info("Marking employee shift {} as present", id);

        EmployeeShift employeeShift = employeeShiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(id));

        if (Boolean.TRUE.equals(employeeShift.getIsPresent())) {
            throw new AlreadyMarkedException(id, "present");
        }

        employeeShift.markPresent(start, end);
        EmployeeShift saved = employeeShiftRepository.save(employeeShift);
        log.info("Employee shift {} marked as present", id);

        return employeeShiftMapper.toResponseDto(saved);
    }

    @Override
    public EmployeeShiftResponseDto markAbsent(Long id) {
        log.info("Marking employee shift {} as absent", id);

        EmployeeShift employeeShift = employeeShiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(id));

        if (Boolean.TRUE.equals(employeeShift.getIsPresent())) {
            throw new AlreadyMarkedException(id, "present");
        }

        employeeShift.markAbsent();
        EmployeeShift saved = employeeShiftRepository.save(employeeShift);
        log.info("Employee shift {} marked as absent", id);

        return employeeShiftMapper.toResponseDto(saved);
    }

    @Override
    public EmployeeShiftResponseDto updateEmployeeShift(Long id, EmployeeShiftUpdateDto dto) {
        log.info("Updating employee shift id: {}", id);

        EmployeeShift employeeShift = employeeShiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(id));

        employeeShiftMapper.updateEntity(dto, employeeShift);
        EmployeeShift saved = employeeShiftRepository.save(employeeShift);
        log.info("Employee shift updated id: {}", saved.getId());

        return employeeShiftMapper.toResponseDto(saved);
    }

    @Override
    public void deleteEmployeeShift(Long id) {
        log.info("Soft-deleting employee shift id: {}", id);

        EmployeeShift employeeShift = employeeShiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmployeeShiftNotFoundException.byId(id));

        employeeShift.softDelete(null);
        employeeShiftRepository.save(employeeShift);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEmployee(Long employeeId) {
        return employeeShiftRepository.countByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPresentByEmployee(Long employeeId) {
        return employeeShiftRepository.countPresentByEmployeeId(employeeId);
    }
}
