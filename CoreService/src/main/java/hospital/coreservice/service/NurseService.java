package hospital.coreservice.service;

import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.nurse.NurseUpdateDto;
import hospital.coreservice.model.enums.NursePosition;

import java.util.List;

/**
 * Service interface for Nurse management.
 * Handles CRUD, department assignment, and shift management.
 *
 * @author Mobina
 */
public interface NurseService {
    NurseResponseDto createNurse(NurseCreateDto nurseCreateDto);
    NurseResponseDto updateNurse(Long nurseId, NurseUpdateDto nurseUpdateDto);
    NurseResponseDto getNurseById(Long nurseId);
    NurseResponseDto getNurseByUserId(Long userId);
    NurseResponseDto getNurseByNurseCode(String nurseCode);
    NurseResponseDto getNurseByNationalId(String nationalId);
    NurseResponseDto getNurseByPhoneNumber(String phoneNumber);
    NurseResponseDto getNurseWithShifts(Long nurseId);
    List<NurseResponseDto> getAllNurses();
    List<NurseResponseDto> searchNursesByName(String firstName, String lastName);
    List<NurseResponseDto> searchActiveNursesByName(String firstName, String lastName);
    List<NurseResponseDto> getNursesByPosition(NursePosition position);
    List<NurseResponseDto> getActiveNursesByPosition(NursePosition position);
    List<NurseResponseDto> getNursesByDepartmentId(Long departmentId);
    List<NurseResponseDto> getNursesByExperienceRange(int min, int max);
    List<NurseResponseDto> getActiveNursesByExperienceRange(int min, int max);
    List<NurseResponseDto> getAllActiveNurses();
    List<NurseResponseDto> getAllInactiveNurses();
    List<NurseResponseDto> getActiveNursesByDepartmentId(Long departmentId);
    void activateNurse(Long nurseId);
    void deactivateNurse(Long nurseId);
    void assignDepartment(Long nurseId, Long departmentId);
    void removeDepartment(Long nurseId, Long departmentId);
    void addShiftPreference(Long nurseId, Long shiftId);
    void removeShiftPreference(Long nurseId, Long shiftId);
    void bulkCreateNurses(List<NurseCreateDto> nurseCreateDtoList);
    Long countNursesByPosition(NursePosition position);
    Long countNursesByDepartmentId(Long departmentId);
    Long countActiveNurses();
    Long countInactiveNurses();
    Long countAllNurses();
    boolean isNurseCodeUnique(String nurseCode);
    boolean isNationalIdUnique(String nationalId);
    boolean existsNurseByUserId(Long userId);
}