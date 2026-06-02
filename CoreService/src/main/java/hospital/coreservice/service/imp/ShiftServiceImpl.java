package hospital.coreservice.service.imp;

import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.dto.shift.ShiftUpdateDto;
import hospital.coreservice.exception.shift.DuplicateShiftNameException;
import hospital.coreservice.exception.shift.ShiftNotFoundException;
import hospital.coreservice.mapper.ShiftMapper;
import hospital.coreservice.model.Shift;
import hospital.coreservice.repository.ShiftRepository;
import hospital.coreservice.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ShiftService.
 *
 * @author Mobina
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    // ========== Create / Update ==========

    @Override
    public ShiftResponseDto createShift(ShiftCreateDto createDto) {
        // Check unique name before saving
        if (!isShiftNameUnique(createDto.getName())) {
            throw new DuplicateShiftNameException(createDto.getName());
        }
        Shift shift = shiftMapper.toEntity(createDto);
        shift = shiftRepository.save(shift);
        return shiftMapper.toResponseDto(shift);
    }

    @Override
    public ShiftResponseDto updateShift(Long shiftId, ShiftUpdateDto updateDto) {
        // Find existing or throw exception
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> ShiftNotFoundException.byId(shiftId));
        shiftMapper.updateEntity(shift, updateDto);
        shift = shiftRepository.save(shift);
        return shiftMapper.toResponseDto(shift);
    }

    // ========== Find by ID / Name ==========

    @Override
    public ShiftResponseDto getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> ShiftNotFoundException.byId(shiftId));
        return shiftMapper.toResponseDto(shift);
    }

    @Override
    public ShiftResponseDto getShiftByName(String name) {
        Shift shift = shiftRepository.findByName(name)
                .orElseThrow(() -> ShiftNotFoundException.byName(name));
        return shiftMapper.toResponseDto(shift);
    }

    // ========== List all shifts ==========

    @Override
    public List<ShiftResponseDto> getAllShifts() {
        return shiftRepository.findAll()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== List by properties ==========

    @Override
    public List<ShiftResponseDto> getShiftsByType(boolean nightShift) {
        if (nightShift) {
            return shiftRepository.findAllNightShifts()
                    .stream()
                    .map(shiftMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
        return shiftRepository.findAllDayShifts()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftResponseDto> getShiftsWithExtraPay() {
        return shiftRepository.findAllShiftsWithExtraPay()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== List by active status ==========

    @Override
    public List<ShiftResponseDto> getActiveShifts() {
        return shiftRepository.findActiveShifts()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftResponseDto> getInactiveShifts() {
        return shiftRepository.findInactiveShifts()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Activation / Deactivation ==========

    @Override
    public void activateShift(Long shiftId) {
        // Check existence before activating
        if (!shiftRepository.existsById(shiftId)) {
            throw ShiftNotFoundException.byId(shiftId);
        }
        shiftRepository.activate(shiftId);
    }

    @Override
    public void deactivateShift(Long shiftId) {
        // Check existence before deactivating
        if (!shiftRepository.existsById(shiftId)) {
            throw ShiftNotFoundException.byId(shiftId);
        }
        shiftRepository.deactivate(shiftId);
    }

    // ========== Count operations ==========

    @Override
    public Long countActiveShifts() {
        return shiftRepository.countActiveShifts();
    }

    @Override
    public Long countInactiveShifts() {
        return shiftRepository.countInactiveShifts();
    }

    @Override
    public Long countAllShifts() {
        return shiftRepository.count();
    }

    // ========== Validation ==========

    @Override
    public boolean isShiftNameUnique(String name) {
        return !shiftRepository.existsByName(name);
    }
}