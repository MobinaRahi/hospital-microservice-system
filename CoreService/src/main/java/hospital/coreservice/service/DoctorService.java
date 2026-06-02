package hospital.coreservice.service;

import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.doctor.DoctorUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;

import java.util.List;

/**
 * Service interface for Doctor management.
 *
 * @author Mobina
 */
public interface DoctorService {

    // ========== Core Operations ==========

    /**
     * Create new doctor
     */
    DoctorResponseDto createDoctor(DoctorCreateDto doctorCreateDto);

    /**
     * Update existing doctor
     */
    DoctorResponseDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto);

    // ========== Basic Retrieval ==========

    /**
     * Get doctor by ID
     */
    DoctorResponseDto getDoctorById(Long doctorId);

    /**
     * Get doctor by user ID (Auth Service)
     */
    DoctorResponseDto getDoctorByUserId(Long userId);

    /**
     * Get doctor by license number
     */
    DoctorResponseDto getDoctorByLicenseNumber(String licenseNumber);

    /**
     * Get all doctors
     */
    List<DoctorResponseDto> getAllDoctors();

    // ========== Filtering & Search ==========

    /**
     * Get doctors by specialty
     */
    List<DoctorResponseDto> getDoctorsBySpeciality(Speciality speciality);

    /**
     * Get doctors by sub-specialty
     */
    List<DoctorResponseDto> getDoctorsBySubSpeciality(SubSpeciality subSpeciality);

    /**
     * Get doctors by department ID
     */
    List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId);

    /**
     * Search doctors by name (partial match, case-insensitive)
     */
    List<DoctorResponseDto> searchDoctorsByName(String firstName, String lastName);

    /**
     * Get doctors by specialty and sub-specialty together
     */
    List<DoctorResponseDto> getDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);

    /**
     * Get doctors by years of experience range
     */
    List<DoctorResponseDto> getDoctorsByExperienceRange(int minYears, int maxYears);

    // ========== Availability ==========

    /**
     * Get doctors available on a specific day of week (based on schedule)
     */
    List<DoctorResponseDto> getAvailableDoctorsByDay(DayOfWeek dayOfWeek);

    // ========== Active/Inactive ==========

    /**
     * Get all active doctors
     */
    List<DoctorResponseDto> getActiveDoctors();

    /**
     * Get all inactive doctors
     */
    List<DoctorResponseDto> getInactiveDoctors();

    /**
     * Get active doctors by specialty
     */
    List<DoctorResponseDto> getActiveDoctorsBySpeciality(Speciality speciality);

    /**
     * Get active doctors by sub-specialty
     */
    List<DoctorResponseDto> getActiveDoctorsBySubSpeciality(SubSpeciality subSpeciality);

    /**
     * Get active doctors by department
     */
    List<DoctorResponseDto> getActiveDoctorsByDepartment(Long departmentId);

    /**
     * Get active doctors by specialty and sub-specialty together
     */
    List<DoctorResponseDto> getActiveDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);

    // ========== Status Management ==========

    /**
     * Activate doctor
     */
    void activateDoctor(Long doctorId);

    /**
     * Deactivate doctor
     */
    void deactivateDoctor(Long doctorId);

    // ========== Sub-Specialty Management ==========

    /**
     * Add sub-specialty to doctor
     */
    void addSubSpeciality(Long doctorId, SubSpeciality subSpeciality);

    /**
     * Remove sub-specialty from doctor
     */
    void removeSubSpeciality(Long doctorId, SubSpeciality subSpeciality);

    // ========== Department Assignment ==========

    /**
     * Assign doctor to department
     */
    void assignDepartment(Long doctorId, Long departmentId);

    /**
     * Remove doctor from department
     */
    void removeDepartment(Long doctorId);

    // ========== Statistics ==========

    /**
     * Count doctors by specialty
     */
    Long countDoctorsBySpeciality(Speciality speciality);

    /**
     * Count doctors by sub-specialty
     */
    Long countDoctorsBySubSpeciality(SubSpeciality subSpeciality);

    /**
     * Count doctors by department ID
     */
    Long countDoctorsByDepartmentId(Long departmentId);

    /**
     * Count active doctors
     */
    Long countActiveDoctors();

    /**
     * Count inactive doctors
     */
    Long countInactiveDoctors();

    /**
     * Count total doctors
     */
    Long countAllDoctors();

    // ========== Validation ==========

    /**
     * Check if license number is unique
     */
    boolean isLicenseNumberUnique(String licenseNumber);
}