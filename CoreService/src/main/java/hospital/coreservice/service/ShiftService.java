package hospital.coreservice.service;

import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.dto.shift.ShiftUpdateDto;

import java.util.List;

public interface ShiftService {
    ShiftResponseDto createShift(ShiftCreateDto createDto);
    ShiftResponseDto updateShift(Long shiftId, ShiftUpdateDto updateDto);
    ShiftResponseDto getShiftById(Long shiftId);
    ShiftResponseDto getShiftByName(String name);
    List<ShiftResponseDto> getAllShifts();
    List<ShiftResponseDto> getShiftsByType(boolean nightShift);
    List<ShiftResponseDto> getActiveShiftsByType(boolean nightShift);
    List<ShiftResponseDto> getShiftsWithExtraPay();
    List<ShiftResponseDto> getActiveShiftsWithExtraPay();
    List<ShiftResponseDto> getActiveShifts();
    List<ShiftResponseDto> getInactiveShifts();
    void activateShift(Long shiftId);
    void deactivateShift(Long shiftId);
    Long countActiveShifts();
    Long countInactiveShifts();
    Long countAllShifts();
    boolean isShiftNameUnique(String name);
}
