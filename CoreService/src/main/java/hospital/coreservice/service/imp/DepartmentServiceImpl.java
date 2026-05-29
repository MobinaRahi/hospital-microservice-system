package hospital.coreservice.service.imp;

import com.hospital.coreService.dto.department.DepartmentCreateDto;
import com.hospital.coreService.dto.department.DepartmentResponseDto;
import com.hospital.coreService.dto.department.DepartmentUpdateDto;
import com.hospital.coreService.dto.doctor.DoctorResponseDto;
import com.hospital.coreService.dto.nurse.NurseResponseDto;
import com.hospital.coreService.dto.room.RoomResponseDto;
import com.hospital.coreService.exception.department.DepartmentHasNoHeadDoctorException;
import com.hospital.coreService.exception.department.DepartmentHasNoHeadNurseException;
import com.hospital.coreService.exception.department.DepartmentNotFoundException;
import com.hospital.coreService.exception.doctor.DoctorNotFoundException;
import com.hospital.coreService.exception.nurse.NurseNotFoundException;
import com.hospital.coreService.exception.room.RoomNotFoundException;
import com.hospital.coreService.mapper.DepartmentMapper;
import com.hospital.coreService.mapper.DoctorMapper;
import com.hospital.coreService.mapper.NurseMapper;
import com.hospital.coreService.mapper.RoomMapper;
import com.hospital.coreService.model.Department;
import com.hospital.coreService.model.Doctor;
import com.hospital.coreService.model.Nurse;
import com.hospital.coreService.model.Room;
import com.hospital.coreService.repository.DepartmentRepository;
import com.hospital.coreService.repository.DoctorRepository;
import com.hospital.coreService.repository.NurseRepository;
import com.hospital.coreService.repository.RoomRepository;
import com.hospital.coreService.service.DepartmentService;
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
@Slf4j
@Service
@RequiredArgsConstructor
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

    @Override
    @Transactional
    public DepartmentResponseDto deleteDepartment(Long id) {
        log.warn("Deleting department id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> DepartmentNotFoundException.byId(id));
        departmentRepository.delete(department);
        log.info("Department deleted id: {}", id);
        return departmentMapper.toResponseDto(department);
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
        departmentRepository.activate(departmentId);
        log.info("Department {} activated", departmentId);
    }

    @Override
    @Transactional
    public void deactivateDepartment(Long departmentId) {
        log.info("Deactivating department {}", departmentId);
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
    public List<DepartmentResponseDto> searchDepartmentsByLocation(String location) {
        log.debug("Searching departments by location: {}", location);
        return departmentRepository.findByLocationContainingIgnoreCase(location)
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
        return roomRepository.countByDepartmentId(departmentId);
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