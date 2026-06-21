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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ShiftResponseDto createShift(ShiftCreateDto createDto) {
        log.info("Creating new shift with name: {}", createDto.getName());
        if (!isShiftNameUnique(createDto.getName())) {
            log.warn("Shift name already exists: {}", createDto.getName());
            throw new DuplicateShiftNameException(createDto.getName());
        }
        Shift shift = shiftMapper.toEntity(createDto);
        shift = shiftRepository.save(shift);
        log.info("Shift created with id: {}", shift.getId());
        return shiftMapper.toResponseDto(shift);
    }

    @Override
    @Transactional
    public ShiftResponseDto updateShift(Long shiftId, ShiftUpdateDto updateDto) {
        log.info("Updating shift with id: {}", shiftId);
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> {
                    log.error("Shift not found with id: {}", shiftId);
                    return ShiftNotFoundException.byId(shiftId);
                });
        shiftMapper.updateEntity(shift, updateDto);
        shift = shiftRepository.save(shift);
        log.info("Shift updated with id: {}", shift.getId());
        return shiftMapper.toResponseDto(shift);
    }

    // ========== Find by ID / Name ==========

    @Override
    public ShiftResponseDto getShiftById(Long shiftId) {
        log.debug("Fetching shift by id: {}", shiftId);
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(()-> ShiftNotFoundException.byId(shiftId));
        return shiftMapper.toResponseDto(shift);
    }

    @Override
    public ShiftResponseDto getShiftByName(String name) {
        log.debug("Fetching shift by name: {}", name);
        Shift shift = shiftRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Shift not found with name: {}", name);
                    return ShiftNotFoundException.byName(name);
                });
        return shiftMapper.toResponseDto(shift);
    }

    // ========== List all shifts ==========

    @Override
    public List<ShiftResponseDto> getAllShifts() {
        log.debug("Fetching all shifts");
        return shiftRepository.findAll()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== List by properties ==========

    @Override
    public List<ShiftResponseDto> getShiftsByType(boolean nightShift) {
        log.debug("Fetching shifts by nightShift = {}", nightShift);
        if (nightShift) {
            return shiftRepository.findAllNightShifts()
                    .stream()
                    .map(shiftMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else {
            return shiftRepository.findAllDayShifts()
                    .stream()
                    .map(shiftMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ShiftResponseDto> getActiveShiftsByType(boolean nightShift) {
        log.debug("Fetching active shifts by nightShift = {}", nightShift);
        return shiftRepository.findAllActive()
                .stream()
                .filter(shift -> shift.isNightShift() == nightShift)
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftResponseDto> getShiftsWithExtraPay() {
        log.debug("Fetching shifts with extra pay");
        return shiftRepository.findAllShiftsWithExtraPay()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftResponseDto> getActiveShiftsWithExtraPay() {
        log.debug("Fetching active shifts with extra pay");
        return shiftRepository.findAllActiveShiftsWithExtraPay()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== List by active status ==========

    @Override
    public List<ShiftResponseDto> getActiveShifts() {
        log.debug("Fetching active shifts");
        return shiftRepository.findAllActive()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftResponseDto> getInactiveShifts() {
        log.debug("Fetching inactive shifts");
        return shiftRepository.findAllInactive()
                .stream()
                .map(shiftMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Activation / Deactivation ==========

    @Override
    @Transactional
    public void activateShift(Long shiftId) {
        log.info("Activating shift with id: {}", shiftId);
        if (!shiftRepository.existsById(shiftId)) {
            log.error("Shift not found for activation, id: {}", shiftId);
            throw ShiftNotFoundException.byId(shiftId);
        }
        shiftRepository.activate(shiftId);
        log.info("Shift activated, id: {}", shiftId);
    }

    @Override
    @Transactional
    public void deactivateShift(Long shiftId) {
        log.warn("Deactivating shift with id: {}", shiftId);
        if (!shiftRepository.existsById(shiftId)) {
            log.error("Shift not found for deactivation, id: {}", shiftId);
            throw ShiftNotFoundException.byId(shiftId);
        }
        shiftRepository.deactivate(shiftId);
        log.info("Shift deactivated, id: {}", shiftId);
    }

    // ========== Count operations ==========

    @Override
    public Long countActiveShifts() {
        log.debug("Counting active shifts");
        return shiftRepository.countActive();
    }

    @Override
    public Long countInactiveShifts() {
        log.debug("Counting inactive shifts");
        return shiftRepository.countInactive();
    }

    @Override
    public Long countAllShifts() {
        log.debug("Counting all shifts");
        return shiftRepository.count();
    }

    // ========== Validation ==========

    @Override
    public boolean isShiftNameUnique(String name) {
        log.debug("Checking uniqueness of shift name: {}", name);
        return !shiftRepository.existsByName(name);
    }
}