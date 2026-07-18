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
 * Handles CRUD, specialty, department assignment, and schedules.
 *
 * @author Mobina
 */
public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorCreateDto doctorCreateDto);
    DoctorResponseDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto);
    DoctorResponseDto getDoctorById(Long doctorId);
    DoctorResponseDto getDoctorByUserId(Long userId);
    DoctorResponseDto getDoctorByLicenseNumber(String licenseNumber);
    List<DoctorResponseDto> getAllDoctors();
    List<DoctorResponseDto> getDoctorsBySpeciality(Speciality speciality);
    List<DoctorResponseDto> getDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId);
    List<DoctorResponseDto> searchDoctorsByName(String firstName, String lastName);
    List<DoctorResponseDto> getDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);
    List<DoctorResponseDto> getDoctorsByExperienceRange(int minYears, int maxYears);
    List<DoctorResponseDto> getAvailableDoctorsByDay(DayOfWeek dayOfWeek);
    List<DoctorResponseDto> getActiveDoctors();
    List<DoctorResponseDto> getInactiveDoctors();
    List<DoctorResponseDto> getActiveDoctorsBySpeciality(Speciality speciality);
    List<DoctorResponseDto> getActiveDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    List<DoctorResponseDto> getActiveDoctorsByDepartmentId(Long departmentId);
    List<DoctorResponseDto> getActiveDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality);
    void activateDoctor(Long doctorId);
    void deactivateDoctor(Long doctorId);
    void addSubSpeciality(Long doctorId, SubSpeciality subSpeciality);
    void removeSubSpeciality(Long doctorId, SubSpeciality subSpeciality);
    void assignDepartment(Long doctorId, Long departmentId);
    void removeDepartment(Long doctorId);
    Long countDoctorsBySpeciality(Speciality speciality);
    Long countDoctorsBySubSpeciality(SubSpeciality subSpeciality);
    Long countDoctorsByDepartmentId(Long departmentId);
    Long countActiveDoctors();
    Long countInactiveDoctors();
    Long countAllDoctors();
    boolean isLicenseNumberUnique(String licenseNumber);
}