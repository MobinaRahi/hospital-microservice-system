package hospital.adminservice.service;

import hospital.adminservice.dto.shift.ShiftCreateDto;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import hospital.adminservice.dto.shift.ShiftUpdateDto;

import java.util.List;

/**
 * Service interface for Shift management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each shift must have a unique code</li>
 *   <li>Shifts can be active or inactive</li>
 *   <li>Night shifts and weekend shifts may have different pay rates</li>
 *   <li>Soft delete supported</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface ShiftService {

    /**
     * Creates a new shift.
     *
     * @param dto the shift creation data
     * @return the created shift
     */
    ShiftResponseDto createShift(ShiftCreateDto dto);

    /**
     * Gets a shift by its ID.
     *
     * @param id the shift ID
     * @return the shift
     */
    ShiftResponseDto getShiftById(Long id);

    /**
     * Gets a shift by its unique code.
     *
     * @param code the shift code
     * @return the shift
     */
    ShiftResponseDto getShiftByCode(String code);

    /**
     * Gets all active shifts.
     *
     * @return list of active shifts
     */
    List<ShiftResponseDto> getAllActiveShifts();

    /**
     * Gets all shifts (including inactive).
     *
     * @return list of all shifts
     */
    List<ShiftResponseDto> getAllShifts();

    /**
     * Gets all night shifts.
     *
     * @return list of night shifts
     */
    List<ShiftResponseDto> getNightShifts();

    /**
     * Updates an existing shift.
     *
     * @param id  the shift ID
     * @param dto the update data
     * @return the updated shift
     */
    ShiftResponseDto updateShift(Long id, ShiftUpdateDto dto);

    /**
     * Toggles the active status of a shift.
     *
     * @param id the shift ID
     * @return the updated shift
     */
    ShiftResponseDto toggleActive(Long id);

    /**
     * Soft-deletes a shift.
     *
     * @param id the shift ID
     */
    void deleteShift(Long id);

    /**
     * Checks if a shift code is already in use.
     *
     * @param code the code to check
     * @return true if the code exists
     */
    boolean codeExists(String code);
}
