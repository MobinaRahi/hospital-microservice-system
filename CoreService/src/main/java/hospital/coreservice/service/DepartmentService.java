package hospital.coreservice.service;

import com.hospital.coreService.dto.department.DepartmentCreateDto;
import com.hospital.coreService.dto.department.DepartmentResponseDto;
import com.hospital.coreService.dto.department.DepartmentUpdateDto;
import com.hospital.coreService.dto.doctor.DoctorResponseDto;
import com.hospital.coreService.dto.nurse.NurseResponseDto;
import com.hospital.coreService.dto.room.RoomResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Department management.
 *
 * @author Mobina
 */
public interface DepartmentService {

    // ========== Core Operations ==========

    /**
     * Create new department
     */
    DepartmentResponseDto createDepartment(DepartmentCreateDto createDto);

    /**
     * Update existing department
     */
    DepartmentResponseDto updateDepartment(Long id, DepartmentUpdateDto updateDto);

    /**
     * Soft delete department (deactivate)
     */
    DepartmentResponseDto deleteDepartment(Long id);

    // ========== Basic Retrieval ==========

    /**
     * Get department by ID
     */
    DepartmentResponseDto getDepartmentById(Long id);

    /**
     * Get department by unique code
     */
    DepartmentResponseDto getDepartmentByCode(String code);

    /**
     * Get department by name
     */
    List<DepartmentResponseDto> getDepartmentByName(String name);
    /**
     * Get all departments
     */
    List<DepartmentResponseDto> getAllDepartments();

    /**
     * Get active departments only
     */
    List<DepartmentResponseDto> getActiveDepartments();

    /**
     * Get inactive departments only
     */
    List<DepartmentResponseDto> getInactiveDepartments();

    // ========== Leadership Management ==========

    /**
     * Assign head doctor to department
     */
    void assignHeadDoctor(Long departmentId, Long doctorId);

    /**
     * Assign head nurse to department
     */
    void assignHeadNurse(Long departmentId, Long nurseId);

    /**
     * Remove head doctor from department
     */
    void removeHeadDoctor(Long departmentId);

    /**
     * Remove head nurse from department
     */
    void removeHeadNurse(Long departmentId);

    /**
     * Get head doctor of department
     */
    DoctorResponseDto getDepartmentHeadDoctor(Long departmentId);

    /**
     * Get head nurse of department
     */
    NurseResponseDto getDepartmentHeadNurse(Long departmentId);

    // ========== Member Management ==========

    /**
     * Add doctor to department
     */
    void addDoctorToDepartment(Long departmentId, Long doctorId);

    /**
     * Add nurse to department
     */
    void addNurseToDepartment(Long departmentId, Long nurseId);

    /**
     * Add room to department
     */
    void addRoomToDepartment(Long departmentId, Long roomId);

    /**
     * Remove doctor from department
     */
    void removeDoctorFromDepartment(Long departmentId, Long doctorId);

    /**
     * Remove nurse from department
     */
    void removeNurseFromDepartment(Long departmentId, Long nurseId);

    /**
     * Remove room from department
     */
    void removeRoomFromDepartment(Long departmentId, Long roomId);

    /**
     * Remove all doctors from department (for bulk transfer)
     */
    void removeAllDoctorsFromDepartment(Long departmentId);

    /**
     * Remove all nurses from department (for bulk transfer)
     */
    void removeAllNursesFromDepartment(Long departmentId);

    // ========== List Retrieval ==========

    /**
     * Get all doctors in department
     */
    List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId);

    /**
     * Get all nurses in department
     */
    List<NurseResponseDto> getNursesByDepartmentId(Long departmentId);

    /**
     * Get all rooms in department
     */
    List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId);

    // ========== Status Management ==========

    /**
     * Activate department
     */
    void activateDepartment(Long departmentId);

    /**
     * Deactivate department
     */
    void deactivateDepartment(Long departmentId);

    // ========== Search & Filter ==========

    /**
     * Search departments by name (partial match, case-insensitive)
     */
    List<DepartmentResponseDto> searchDepartmentsByName(String name);

    /**
     * Search departments by location (partial match, case-insensitive)
     */
    List<DepartmentResponseDto> searchDepartmentsByLocation(String location);

    /**
     * Get departments by location (exact match)
     */
    List<DepartmentResponseDto> getDepartmentsByLocation(String location);

    /**
     * Get departments by status (active/inactive)
     */
    List<DepartmentResponseDto> getDepartmentsByStatus(boolean isActive);

    // ========== Statistics ==========

    /**
     * Count doctors in department
     */
    Long countDoctorsInDepartment(Long departmentId);

    /**
     * Count nurses in department
     */
    Long countNursesInDepartment(Long departmentId);

    /**
     * Count rooms in department
     */
    Long countRoomsInDepartment(Long departmentId);

    /**
     * Count total departments
     */
    Long countTotalDepartments();

    /**
     * Count active departments
     */
    Long countActiveDepartments();

    // ========== Validation ==========

    /**
     * Check if department code is unique (for validation)
     */
    boolean isDepartmentCodeUnique(String code);

    /**
     * Check if department name already exists
     */
    boolean existsDepartmentByName(String name);

    /**
     * Check if department exists by code
     */
    boolean existsDepartmentByCode(String code);

    /**
     * Check if department exists by ID
     */
    boolean existsDepartmentById(Long id);
}