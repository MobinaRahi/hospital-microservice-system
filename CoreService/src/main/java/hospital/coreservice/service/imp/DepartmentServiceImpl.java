package hospital.coreservice.service.imp;

import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.department.DepartmentUpdateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.exception.department.DepartmentHasNoHeadDoctorException;
import hospital.coreservice.exception.department.DepartmentHasNoHeadNurseException;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.exception.nurse.NurseNotFoundException;
import hospital.coreservice.exception.room.RoomNotFoundException;
import hospital.coreservice.mapper.DepartmentMapper;
import hospital.coreservice.mapper.DoctorMapper;
import hospital.coreservice.mapper.NurseMapper;
import hospital.coreservice.mapper.RoomMapper;
import hospital.coreservice.model.Department;
import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.Nurse;
import hospital.coreservice.model.Room;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.DoctorRepository;
import hospital.coreservice.repository.NurseRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DepartmentService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentCreateDto createDto) {
        log.info("Creating department with code: {}", createDto.getDepartmentCode());
        Department department = departmentMapper.toEntity(createDto);
        Department saved = departmentRepository.save(department);
        log.info("Department created with id: {}", saved.getId());
        return departmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(Long id, DepartmentUpdateDto updateDto) {
        log.info("Updating department id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> DepartmentNotFoundException.byId(id));
        departmentMapper.updateEntity(department, updateDto);
        Department updated = departmentRepository.save(department);
        log.info("Department updated id: {}", id);
        return departmentMapper.toResponseDto(updated);
    }


    // ========== Basic Retrieval ==========

    @Override
    public DepartmentResponseDto getDepartmentById(Long id) {
        log.debug("Fetching department by id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> DepartmentNotFoundException.byId(id));
        return departmentMapper.toResponseDto(department);
    }

    @Override
    public DepartmentResponseDto getDepartmentByCode(String code) {
        log.debug("Fetching department by code: {}", code);
        Department department = departmentRepository.findByDepartmentCode(code)
                .orElseThrow(() -> DepartmentNotFoundException.byCode(code));
        return departmentMapper.toResponseDto(department);
    }

    @Override
    public DepartmentResponseDto getDepartmentByCodeAndIsActiveTrue(String code) {
        log.debug("Fetching activeDepartment by code: {}", code);
        Department department = departmentRepository.findByDepartmentCodeAndIsActiveTrue(code)
                .orElseThrow(() -> DepartmentNotFoundException.byCode(code));
        return departmentMapper.toResponseDto(department);
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentByName(String name) {
        log.debug("Fetching departments by name: {}", name);
        List<Department> departments = departmentRepository.findByDepartmentName(name);
        if (departments.isEmpty()) {
            throw DepartmentNotFoundException.byName(name);
        }
        return departments.stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentByNameAndIsActiveTru(String name) {
        log.debug("Fetching activeDepartment by name: {}", name);
        List<Department> departments = departmentRepository.findByDepartmentNameAndIsActiveTrue(name);
        if (departments.isEmpty()) {
            throw DepartmentNotFoundException.byName(name);
        }
        return departments.stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        log.debug("Fetching all departments");
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getActiveDepartments() {
        log.debug("Fetching active departments");
        return departmentRepository.findByIsActiveTrue()
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getInactiveDepartments() {
        log.debug("Fetching inactive departments");
        return departmentRepository.findByIsActiveFalse()
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Leadership Management ==========

    @Override
    @Transactional
    public void assignHeadDoctor(Long departmentId, Long doctorId) {
        log.info("Assigning doctor {} as head of department {}", doctorId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        department.setHeadDoctor(doctor);
        departmentRepository.save(department);
        log.info("Doctor {} assigned as head of department {}", doctorId, departmentId);
    }

    @Override
    @Transactional
    public void assignHeadNurse(Long departmentId, Long nurseId) {
        log.info("Assigning nurse {} as head of department {}", nurseId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        department.setHeadNurse(nurse);
        departmentRepository.save(department);
        log.info("Nurse {} assigned as head of department {}", nurseId, departmentId);
    }

    @Override
    @Transactional
    public void removeHeadDoctor(Long departmentId) {
        log.info("Removing head doctor from department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        department.setHeadDoctor(null);
        departmentRepository.save(department);
        log.info("Head doctor removed from department {}", departmentId);
    }

    @Override
    @Transactional
    public void removeHeadNurse(Long departmentId) {
        log.info("Removing head nurse from department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        department.setHeadNurse(null);
        departmentRepository.save(department);
        log.info("Head nurse removed from department {}", departmentId);
    }

    @Override
    public DoctorResponseDto getDepartmentHeadDoctor(Long departmentId) {
        log.debug("Fetching head doctor of department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        if (department.getHeadDoctor() == null) {
            throw new DepartmentHasNoHeadDoctorException(departmentId);
        }
        return doctorMapper.toResponseDto(department.getHeadDoctor());
    }

    @Override
    public NurseResponseDto getDepartmentHeadNurse(Long departmentId) {
        log.debug("Fetching head nurse of department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        if (department.getHeadNurse() == null) {
            throw new DepartmentHasNoHeadNurseException(departmentId);
        }
        return nurseMapper.toResponseDto(department.getHeadNurse());
    }

    // ========== Member Management ==========

    @Override
    @Transactional
    public void addDoctorToDepartment(Long departmentId, Long doctorId) {
        log.info("Adding doctor {} to department {}", doctorId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        department.addDoctor(doctor);
        departmentRepository.save(department);
        log.info("Doctor {} added to department {}", doctorId, departmentId);
    }

    @Override
    @Transactional
    public void addNurseToDepartment(Long departmentId, Long nurseId) {
        log.info("Adding nurse {} to department {}", nurseId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        department.addNurse(nurse);
        departmentRepository.save(department);
        log.info("Nurse {} added to department {}", nurseId, departmentId);
    }

    @Override
    @Transactional
    public void addRoomToDepartment(Long departmentId, Long roomId) {
        log.info("Adding room {} to department {}", roomId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        department.addRoom(room);
        departmentRepository.save(department);
        log.info("Room {} added to department {}", roomId, departmentId);
    }

    @Override
    @Transactional
    public void removeDoctorFromDepartment(Long departmentId, Long doctorId) {
        log.info("Removing doctor {} from department {}", doctorId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> DoctorNotFoundException.byId(doctorId));
        department.removeDoctor(doctor);
        departmentRepository.save(department);
        log.info("Doctor {} removed from department {}", doctorId, departmentId);
    }

    @Override
    @Transactional
    public void removeNurseFromDepartment(Long departmentId, Long nurseId) {
        log.info("Removing nurse {} from department {}", nurseId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        department.removeNurse(nurse);
        departmentRepository.save(department);
        log.info("Nurse {} removed from department {}", nurseId, departmentId);
    }

    @Override
    @Transactional
    public void removeRoomFromDepartment(Long departmentId, Long roomId) {
        log.info("Removing room {} from department {}", roomId, departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        department.removeRoom(room);
        departmentRepository.save(department);
        log.info("Room {} removed from department {}", roomId, departmentId);
    }

    @Override
    @Transactional
    public void removeAllDoctorsFromDepartment(Long departmentId) {
        log.info("Removing all doctors from department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        department.getDoctorList().forEach(doctor -> doctor.setDepartment(null));
        department.getDoctorList().clear();
        departmentRepository.save(department);
        log.info("All doctors removed from department {}", departmentId);
    }

    @Override
    @Transactional
    public void removeAllNursesFromDepartment(Long departmentId) {
        log.info("Removing all nurses from department {}", departmentId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        department.getNurseList().forEach(nurse -> nurse.getDepartmentList().remove(department));
        department.getNurseList().clear();
        departmentRepository.save(department);
        log.info("All nurses removed from department {}", departmentId);
    }

    // ========== List Retrieval ==========

    @Override
    public List<DoctorResponseDto> getDoctorsByDepartmentId(Long departmentId) {
        log.debug("Fetching doctors in department {}", departmentId);
        if (!departmentRepository.existsById(departmentId)) {
            throw DepartmentNotFoundException.byId(departmentId);
        }
        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(doctorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getNursesByDepartmentId(Long departmentId) {
        log.debug("Fetching nurses in department {}", departmentId);
        if (!departmentRepository.existsById(departmentId)) {
            throw DepartmentNotFoundException.byId(departmentId);
        }
        return nurseRepository.findByDepartmentId(departmentId)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId) {
        log.debug("Fetching rooms in department {}", departmentId);
        if (!departmentRepository.existsById(departmentId)) {
            throw DepartmentNotFoundException.byId(departmentId);
        }
        return roomRepository.findByDepartmentId(departmentId)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Status Management ==========

    @Override
    @Transactional
    public void activateDepartment(Long departmentId) {
        log.info("Activating department {}", departmentId);
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));

        departmentRepository.activate(departmentId);
        log.info("Department {} activated", departmentId);
    }

    @Override
    @Transactional
    public void deactivateDepartment(Long departmentId) {
        log.info("Deactivating department {}", departmentId);
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        departmentRepository.deactivate(departmentId);
        log.info("Department {} deactivated", departmentId);
    }

    // ========== Search & Filter ==========

    @Override
    public List<DepartmentResponseDto> searchDepartmentsByName(String name) {
        log.debug("Searching departments by name: {}", name);
        return departmentRepository.findByDepartmentNameContainingIgnoreCase(name)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> searchDepartmentsByNameAndIsActiveTrue(String name) {
        log.debug("Searching activeDepartments by name: {}", name);
        return departmentRepository.findByDepartmentNameContainingIgnoreCaseAndIsActiveTrue(name)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> searchDepartmentsByLocation(String location) {
        log.debug("Searching departments by location: {}", location);
        return departmentRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> searchDepartmentsByLocationAndIsActiveTrue(String location) {
        log.debug("Searching activeDepartments by location: {}", location);
        return departmentRepository.findByLocationContainingIgnoreCaseAndIsActiveTrue(location)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentsByLocation(String location) {
        log.debug("Getting departments by exact location: {}", location);
        return departmentRepository.findByLocation(location)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentsByLocationAndIsActiveTrue(String location) {
        log.debug("Getting activeDepartments by exact location: {}", location);
        return departmentRepository.findByLocationAndIsActiveTrue(location)
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentsByStatus(boolean isActive) {
        log.debug("Getting departments by status: active={}", isActive);
        if (isActive) {
            return departmentRepository.findByIsActiveTrue()
                    .stream()
                    .map(departmentMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else {
            return departmentRepository.findByIsActiveFalse()
                    .stream()
                    .map(departmentMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
    }

    // ========== Statistics ==========

    @Override
    public Long countDoctorsInDepartment(Long departmentId) {
        log.debug("Counting doctors in department {}", departmentId);
        return doctorRepository.countByDepartmentId(departmentId);
    }

    @Override
    public Long countNursesInDepartment(Long departmentId) {
        log.debug("Counting nurses in department {}", departmentId);
        return nurseRepository.countByDepartmentId(departmentId);
    }

    @Override
    public Long countRoomsInDepartment(Long departmentId) {
        log.debug("Counting rooms in department {}", departmentId);
        return roomRepository.countRoomsByDepartmentId(departmentId);
    }

    @Override
    public Long countTotalDepartments() {
        log.debug("Counting total departments");
        return departmentRepository.count();
    }

    @Override
    public Long countActiveDepartments() {
        log.debug("Counting active departments");
        return departmentRepository.countByIsActiveTrue();
    }

    @Override
    public Long countInactiveDepartments() {
        log.debug("Counting inActive departments");
        return departmentRepository.countByIsActiveFalse();
    }

    // ========== Validation ==========

    @Override
    public boolean isDepartmentCodeUnique(String code) {
        log.debug("Checking if department code is unique: {}", code);
        return !departmentRepository.existsByDepartmentCode(code);
    }

    @Override
    public boolean existsDepartmentByName(String name) {
        log.debug("Checking if department exists by name: {}", name);
        return departmentRepository.existsByDepartmentName(name);
    }

    @Override
    public boolean existsDepartmentByCode(String code) {
        log.debug("Checking if department exists by code: {}", code);
        return departmentRepository.existsByDepartmentCode(code);
    }

    @Override
    public boolean existsDepartmentById(Long id) {
        log.debug("Checking if department exists by id: {}", id);
        return departmentRepository.existsById(id);
    }
}