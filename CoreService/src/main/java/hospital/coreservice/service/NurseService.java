package hospital.coreservice.service;

import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.nurse.NurseUpdateDto;
import hospital.coreservice.model.enums.NursePosition;

import java.util.List;

/**
 * Service interface for Nurse management.
 *
 * @author Mobina
 */
public interface NurseService {

    // ========== Core Operations ==========

    /**
     * Create new nurse
     */
    NurseResponseDto createNurse(NurseCreateDto nurseCreateDto);

    /**
     * Update existing nurse
     */
    NurseResponseDto updateNurse(Long nurseId, NurseUpdateDto nurseUpdateDto);

    // ========== Basic Retrieval ==========

    /**
     * Get nurse by ID
     */
    NurseResponseDto getNurseById(Long nurseId);

    /**
     * Get nurse by user ID (Auth Service)
     */
    NurseResponseDto getNurseByUserId(Long userId);

    /**
     * Get nurse by nurse code
     */
    NurseResponseDto getNurseByNurseCode(String nurseCode);

    /**
     * Get nurse by national ID
     */
    NurseResponseDto getNurseByNationalId(String nationalId);

    /**
     * Get nurse by phone number
     */
    NurseResponseDto getNurseByPhoneNumber(String phoneNumber);

    /**
     * Get nurse with shift preferences (eager loading)
     */
    NurseResponseDto getNurseWithShifts(Long nurseId);

    /**
     * Get all nurses
     */
    List<NurseResponseDto> getAllNurses();

    // ========== Search & Filter ==========

    /**
     * Search nurses by name (partial match, case-insensitive)
     */
    List<NurseResponseDto> searchNursesByName(String firstName, String lastName);
    List<NurseResponseDto> searchActiveNursesByName(String firstName, String lastName);

    /**
     * Get nurses by position
     */
    List<NurseResponseDto> getNursesByPosition(NursePosition position);
    List<NurseResponseDto> getActiveNursesByPosition(NursePosition position);

    /**
     * Get nurses by department ID
     */
    List<NurseResponseDto> getNursesByDepartmentId(Long departmentId);

    /**
     * Get nurses by years of experience range
     */
    List<NurseResponseDto> getNursesByExperienceRange(int min, int max);
    List<NurseResponseDto> getActiveNursesByExperienceRange(int min, int max);

    // ========== Status Based ==========

    /**
     * Get all active nurses
     */
    List<NurseResponseDto> getAllActiveNurses();

    /**
     * Get all inactive nurses
     */
    List<NurseResponseDto> getAllInactiveNurses();

    /**
     * Get active nurses by department ID
     */
    List<NurseResponseDto> getActiveNursesByDepartmentId(Long departmentId);

    // ========== Status Management ==========

    /**
     * Activate nurse
     */
    void activateNurse(Long nurseId);

    /**
     * Deactivate nurse
     */
    void deactivateNurse(Long nurseId);

    // ========== Department Assignment ==========

    /**
     * Assign department to nurse (Many-to-Many)
     */
    void assignDepartment(Long nurseId, Long departmentId);

    /**
     * Remove department from nurse
     */
    void removeDepartment(Long nurseId, Long departmentId);

    // ========== Shift Preference Management ==========

    /**
     * Add shift preference to nurse
     */
    void addShiftPreference(Long nurseId, Long shiftId);

    /**
     * Remove shift preference from nurse
     */
    void removeShiftPreference(Long nurseId, Long shiftId);

    // ========== Bulk Operation ==========

    /**
     * Create multiple nurses at once
     */
    List<NurseResponseDto> bulkCreateNurses(List<NurseCreateDto> nurseCreateDtoList);

    // ========== Statistics ==========

    /**
     * Count nurses by position
     */
    Long countNursesByPosition(NursePosition position);

    /**
     * Count nurses by department ID
     */
    Long countNursesByDepartmentId(Long departmentId);

    /**
     * Count active nurses
     */
    Long countActiveNurses();

    /**
     * Count inactive nurses
     */
    Long countInactiveNurses();

    /**
     * Count total nurses
     */
    Long countAllNurses();

    // ========== Validation ==========

    /**
     * Check if nurse code is unique
     */
    boolean isNurseCodeUnique(String nurseCode);

    /**
     * Check if national ID is unique
     */
    boolean isNationalIdUnique(String nationalId);

    /**
     * Check if nurse exists by user ID
     */
    boolean existsNurseByUserId(Long userId);
}