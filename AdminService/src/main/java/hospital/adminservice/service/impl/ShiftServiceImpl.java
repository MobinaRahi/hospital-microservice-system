package hospital.adminservice.service.impl;

import hospital.adminservice.dto.shift.ShiftCreateDto;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import hospital.adminservice.dto.shift.ShiftUpdateDto;
import hospital.adminservice.exception.shift.DuplicateShiftCodeException;
import hospital.adminservice.exception.shift.ShiftNotFoundException;
import hospital.adminservice.mapper.ShiftMapper;
import hospital.adminservice.model.Shift;
import hospital.adminservice.repository.ShiftRepository;
import hospital.adminservice.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    @Override
    public ShiftResponseDto createShift(ShiftCreateDto dto) {
        log.info("Creating shift: {}", dto.getName());

        if (shiftRepository.existsByCode(dto.getCode())) {
            throw new DuplicateShiftCodeException(dto.getCode());
        }

        Shift shift = shiftMapper.toEntity(dto);
        Shift saved = shiftRepository.save(shift);
        log.info("Shift created with id: {}", saved.getId());

        return shiftMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftResponseDto getShiftById(Long id) {
        log.debug("Fetching shift by id: {}", id);

        Shift shift = shiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> ShiftNotFoundException.byId(id));

        return shiftMapper.toResponseDto(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftResponseDto getShiftByCode(String code) {
        log.debug("Fetching shift by code: {}", code);

        Shift shift = shiftRepository.findByCode(code)
                .orElseThrow(() -> ShiftNotFoundException.byCode(code));

        return shiftMapper.toResponseDto(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponseDto> getAllActiveShifts() {
        log.debug("Fetching all active shifts");

        List<Shift> shifts = shiftRepository.findByIsActiveTrue();
        return shiftMapper.toResponseDtoList(shifts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponseDto> getAllShifts() {
        log.debug("Fetching all shifts");

        List<Shift> shifts = shiftRepository.findAllNotDeleted();
        return shiftMapper.toResponseDtoList(shifts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponseDto> getNightShifts() {
        log.debug("Fetching night shifts");

        List<Shift> shifts = shiftRepository.findByNightShiftTrue();
        return shiftMapper.toResponseDtoList(shifts);
    }

    @Override
    public ShiftResponseDto updateShift(Long id, ShiftUpdateDto dto) {
        log.info("Updating shift id: {}", id);

        Shift shift = shiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> ShiftNotFoundException.byId(id));

        shiftMapper.updateEntity(dto, shift);
        Shift saved = shiftRepository.save(shift);
        log.info("Shift updated id: {}", saved.getId());

        return shiftMapper.toResponseDto(saved);
    }

    @Override
    public ShiftResponseDto toggleActive(Long id) {
        log.info("Toggling active status for shift id: {}", id);

        Shift shift = shiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> ShiftNotFoundException.byId(id));

        shift.setIsActive(!shift.getIsActive());
        Shift saved = shiftRepository.save(shift);

        return shiftMapper.toResponseDto(saved);
    }

    @Override
    public void deleteShift(Long id) {
        log.info("Soft-deleting shift id: {}", id);

        Shift shift = shiftRepository.findNotDeletedById(id)
                .orElseThrow(() -> ShiftNotFoundException.byId(id));

        shift.softDelete(null);
        shiftRepository.save(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return shiftRepository.existsByCode(code);
    }
}
