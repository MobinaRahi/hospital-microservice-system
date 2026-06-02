package hospital.coreservice.service.imp;

import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.doctor.DoctorUpdateDto;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.mapper.DoctorMapper;
import hospital.coreservice.model.Department;
import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.DoctorRepository;
import hospital.coreservice.repository.DoctorScheduleRepository;
import hospital.coreservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DepartmentRepository departmentRepository;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public DoctorResponseDto createDoctor(DoctorCreateDto doctorCreateDto) {
        log.info("Creating new doctor with license: {}", doctorCreateDto.getLicenseNumber());
        Doctor doctor = doctorMapper.toEntity(doctorCreateDto);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public DoctorResponseDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto) {
        log.info("Updating doctor with id: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctorMapper.updateEntity(doctor, doctorUpdateDto);
        Doctor updated = doctorRepository.save(doctor);
        return doctorMapper.toResponseDto(updated);
    }

    // ========== Basic Retrieval ==========

    @Override
    public DoctorResponseDto getDoctorById(Long doctorId) {
        log.debug("Fetching doctor by id: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        return doctorMapper.toResponseDto(doctor);
    }

    @Override
    public DoctorResponseDto getDoctorByUserId(Long userId) {
        log.debug("Fetching doctor by user id: {}", userId);
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> DoctorNotFoundException.byId(userId));
        return doctorMapper.toResponseDto(doctor);
    }

    @Override
    public DoctorResponseDto getDoctorByLicenseNumber(String licenseNumber) {
        log.debug("Fetching doctor by license: {}", licenseNumber);
        Doctor doctor = doctorRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> DoctorNotFoundException.byLicenseNumber(licenseNumber));
        return doctorMapper.toResponseDto(doctor);
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        log.debug("Fetching all doctors");
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Filtering & Search ==========

    @Override
    public List<DoctorResponseDto> getDoctorsBySpeciality(Speciality speciality) {
        log.debug("Fetching doctors by specialty: {}", speciality);
        return doctorRepository.findBySpeciality(speciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getDoctorsBySubSpeciality(SubSpeciality subSpeciality) {
        log.debug("Fetching doctors by sub-specialty: {}", subSpeciality);
        return doctorRepository.findBySubSpeciality(subSpeciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId) {
        log.debug("Fetching doctors by department id: {}", departmentId);
        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> searchDoctorsByName(String firstName, String lastName) {
        log.debug("Searching doctors by name: firstName={}, lastName={}", firstName, lastName);

        if (firstName == null && lastName == null) {
            return getAllDoctors();
        }
        else if (firstName != null && lastName != null) {
            return doctorRepository
                    .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName)
                    .stream()
                    .map(doctorMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
        else if (firstName != null) {
            return doctorRepository.findByFirstNameContainingIgnoreCase(firstName)
                    .stream()
                    .map(doctorMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
        return doctorRepository.findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality) {
        log.debug("Fetching doctors by specialty: {} and sub-specialty: {}", speciality, subSpeciality);
        return doctorRepository.findBySpecialityAndSubSpeciality(speciality, subSpeciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getActiveDoctorsBySpecialityAndSubSpeciality(Speciality speciality, SubSpeciality subSpeciality) {
        log.debug("Fetching active doctors by specialty: {} and sub-specialty: {}", speciality, subSpeciality);
        return doctorRepository.findActiveDoctorsBySpecialityAndSubSpeciality(speciality, subSpeciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getDoctorsByExperienceRange(int minYears, int maxYears) {
        log.debug("Fetching doctors by experience range: {} - {} years", minYears, maxYears);
        return doctorRepository.findByYearsOfExperienceBetween(minYears, maxYears)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Availability ==========

    @Override
    public List<DoctorResponseDto> getAvailableDoctorsByDay(DayOfWeek dayOfWeek) {
        log.debug("Fetching available doctors by day: {}", dayOfWeek);
        return doctorScheduleRepository.findByDayOfWeek(dayOfWeek)
                .stream()
                .map(DoctorSchedule::getDoctor)
                .filter(Doctor::isActive)
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Active/Inactive ==========

    @Override
    public List<DoctorResponseDto> getActiveDoctors() {
        log.debug("Fetching all active doctors");
        return doctorRepository.findAllActiveDoctors()
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getInactiveDoctors() {
        log.debug("Fetching all inactive doctors");
        return doctorRepository.findAllInactiveDoctors()
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getActiveDoctorsBySpeciality(Speciality speciality) {
        log.debug("Fetching active doctors by specialty: {}", speciality);
        return doctorRepository.findActiveBySpeciality(speciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getActiveDoctorsBySubSpeciality(SubSpeciality subSpeciality) {
        log.debug("Fetching active doctors by sub-specialty: {}", subSpeciality);
        return doctorRepository.findActiveBySubSpeciality(subSpeciality)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getActiveDoctorsByDepartment(Long departmentId) {
        log.debug("Fetching active doctors by department id: {}", departmentId);
        return doctorRepository.findActiveByDepartmentId(departmentId)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Status Management ==========

    @Override
    @Transactional
    public void activateDoctor(Long doctorId) {
        log.info("Activating doctor with id: {}", doctorId);
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctorRepository.activate(doctorId);
    }

    @Override
    @Transactional
    public void deactivateDoctor(Long doctorId) {
        log.warn("Deactivating doctor with id: {}", doctorId);
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctorRepository.deactivate(doctorId);
    }

    // ========== Sub-Specialty Management ==========

    @Override
    @Transactional
    public void addSubSpeciality(Long doctorId, SubSpeciality subSpeciality) {
        log.info("Adding sub-specialty {} to doctor id: {}", subSpeciality, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctor.addSubSpeciality(subSpeciality);
        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void removeSubSpeciality(Long doctorId, SubSpeciality subSpeciality) {
        log.info("Removing sub-specialty {} from doctor id: {}", subSpeciality, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctor.removeSubSpeciality(subSpeciality);
        doctorRepository.save(doctor);
    }

    // ========== Department Assignment ==========

    @Override
    @Transactional
    public void assignDepartment(Long doctorId, Long departmentId) {
        log.info("Assigning doctor {} to department {}", doctorId, departmentId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        doctor.setDepartment(department);
        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public void removeDepartment(Long doctorId) {
        log.info("Removing doctor {} from department", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        doctor.setDepartment(null);
        doctorRepository.save(doctor);
    }

    // ========== Statistics ==========

    @Override
    public Long countDoctorsBySpeciality(Speciality speciality) {
        log.debug("Counting doctors by specialty: {}", speciality);
        return doctorRepository.countBySpeciality(speciality);
    }

    @Override
    public Long countDoctorsBySubSpeciality(SubSpeciality subSpeciality) {
        log.debug("Counting active doctors by sub-specialty: {}", subSpeciality);
        return doctorRepository.countActiveBySubSpeciality(subSpeciality);
    }

    @Override
    public Long countDoctorsByDepartmentId(Long departmentId) {
        log.debug("Counting doctors by department id: {}", departmentId);
        return doctorRepository.countByDepartmentId(departmentId);
    }

    @Override
    public Long countAllDoctors() {
        log.debug("Counting all doctors");
        return doctorRepository.count();
    }

    @Override
    public Long countActiveDoctors() {
        log.debug("Counting active doctors");
        return doctorRepository.countActiveDoctors();
    }

    @Override
    public Long countInactiveDoctors() {
        log.debug("Counting inactive doctors");
        return doctorRepository.countInactiveDoctors();
    }

    // ========== Validation ==========

    @Override
    public boolean isLicenseNumberUnique(String licenseNumber) {
        log.debug("Checking if license number is unique: {}", licenseNumber);
        return !doctorRepository.existsByLicenseNumber(licenseNumber);
    }
}