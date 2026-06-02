package hospital.coreservice.service;

import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.dto.shift.ShiftUpdateDto;

import java.util.List;

/**
 * Service interface for shift management.
 *
 * @author Mobina
 */
public interface ShiftService {

    // ========== Create / Update ==========

    /**
     * Creates a new shift.
     */
    ShiftResponseDto createShift(ShiftCreateDto createDto);

    /**
     * Updates an existing shift.
     */
    ShiftResponseDto updateShift(Long shiftId, ShiftUpdateDto updateDto);

    // ========== Find by ID / Name ==========

    /**
     * Finds shift by ID.
     */
    ShiftResponseDto getShiftById(Long shiftId);

    /**
     * Finds shift by exact name (unique).
     */
    ShiftResponseDto getShiftByName(String name);

    // ========== List all shifts ==========

    /**
     * Retrieves all shifts (both active and inactive).
     */
    List<ShiftResponseDto> getAllShifts();

    // ========== List by properties ==========

    /**
     * Retrieves shifts by night/day status (true = night, false = day).
     */
    List<ShiftResponseDto> getShiftsByType(boolean nightShift);
    List<ShiftResponseDto> getActiveShiftsByType(boolean nightShift);

    /**
     * Retrieves shifts that have extra pay.
     */
    List<ShiftResponseDto> getShiftsWithExtraPay();
    List<ShiftResponseDto> getActiveShiftsWithExtraPay();

    // ========== List by active status ==========

    /**
     * Retrieves only active shifts.
     */
    List<ShiftResponseDto> getActiveShifts();

    /**
     * Retrieves only inactive shifts.
     */
    List<ShiftResponseDto> getInactiveShifts();

    // ========== Activation / Deactivation ==========

    /**
     * Activates a shift (soft delete restore).
     */
    void activateShift(Long shiftId);

    /**
     * Deactivates a shift (soft delete).
     */
    void deactivateShift(Long shiftId);

    // ========== Count operations ==========

    /**
     * Returns count of active shifts.
     */
    Long countActiveShifts();

    /**
     * Returns count of inactive shifts.
     */
    Long countInactiveShifts();

    /**
     * Returns total count of shifts (active + inactive).
     */
    Long countAllShifts();

    // ========== Validation ==========

    /**
     * Checks if shift name is unique.
     */
    boolean isShiftNameUnique(String name);
}
