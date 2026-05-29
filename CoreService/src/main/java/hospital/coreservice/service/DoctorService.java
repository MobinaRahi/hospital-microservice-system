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

    DoctorResponseDto createDoctor(DoctorCreateDto doctorCreateDto);
    DoctorResponseDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto);
    void deleteDoctor(Long doctorId);

    // ========== Basic Retrieval ==========

    DoctorResponseDto getDoctorById(Long doctorId);
    DoctorResponseDto getDoctorByUserId(Long userId);
    DoctorResponseDto getDoctorByLicenseNumber(String licenseNumber);
    List<DoctorResponseDto> getAllDoctors();

    // ========== Filtering & Search ==========

    List<DoctorResponseDto> getDoctorsBySpeciality(Speciality speciality);
    List<DoctorResponseDto> getDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId);
    List<DoctorResponseDto> searchDoctorsByName(String firstName, String lastName);
    List<DoctorResponseDto> getDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);
    List<DoctorResponseDto> getDoctorsByExperienceRange(int minYears, int maxYears);

    // ========== Availability ==========

    List<DoctorResponseDto> getAvailableDoctorsByDay(DayOfWeek dayOfWeek);

    // ========== Active/Inactive ==========

    List<DoctorResponseDto> getActiveDoctors();
    List<DoctorResponseDto> getInactiveDoctors();
    List<DoctorResponseDto> getActiveDoctorsBySpeciality(Speciality speciality);
    List<DoctorResponseDto> getActiveDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    List<DoctorResponseDto> getActiveDoctorsByDepartment(Long departmentId);
    List<DoctorResponseDto> getActiveDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);

    // ========== Status Management ==========

    void activateDoctor(Long doctorId);
    void deactivateDoctor(Long doctorId);

    // ========== Sub-Specialty Management ==========

    void addSubSpeciality(Long doctorId, SubSpeciality subSpeciality);
    void removeSubSpeciality(Long doctorId, SubSpeciality subSpeciality);

    // ========== Department Assignment ==========

    void assignDepartment(Long doctorId, Long departmentId);
    void removeDepartment(Long doctorId);

    // ========== Statistics ==========

    Long countDoctorsBySpeciality(Speciality speciality);
    Long countDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    Long countDoctorsByDepartmentId(Long departmentId);
    Long countActiveDoctors();
    Long countInactiveDoctors();
    Long countAllDoctors();

    // ========== Validation ==========

    boolean isLicenseNumberUnique(String licenseNumber);
}