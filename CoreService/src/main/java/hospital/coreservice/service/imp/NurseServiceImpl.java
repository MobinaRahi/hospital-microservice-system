package hospital.coreservice.service.imp;

import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.nurse.NurseUpdateDto;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.nurse.NurseAlreadyExistsException;
import hospital.coreservice.exception.nurse.NurseNotFoundException;
import hospital.coreservice.exception.shift.ShiftNotFoundException;
import hospital.coreservice.exception.user.UserNotFoundException;
import hospital.coreservice.mapper.NurseMapper;
import hospital.coreservice.model.Department;
import hospital.coreservice.model.Nurse;
import hospital.coreservice.model.Shift;
import hospital.coreservice.model.User;
import hospital.coreservice.model.enums.NursePosition;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.NurseRepository;
import hospital.coreservice.repository.ShiftRepository;
import hospital.coreservice.repository.UserRepository;
import hospital.coreservice.service.NurseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of NurseService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;
    private final DepartmentRepository departmentRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public NurseResponseDto createNurse(NurseCreateDto createDto) {
        log.info("Creating new nurse with code: {}", createDto.getNurseCode());
        User user = userRepository.findById(createDto.getUserId())
                .orElseThrow(() ->  UserNotFoundException.byId(createDto.getUserId()));
        Nurse nurse = nurseMapper.toEntity(createDto);
        nurse.setUser(user);
        if (createDto.getYearsOfExperience() != null) {
            nurse.setYearsOfExperience(createDto.getYearsOfExperience());
        }
    if (createDto.getDepartmentIds() != null && !createDto.getDepartmentIds().isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(createDto.getDepartmentIds());
            if (departments.size() != createDto.getDepartmentIds().size()) {
                log.warn("Some department IDs were not found");
            }
            nurse.setDepartmentList(departments);
        }
        Nurse saved = nurseRepository.save(nurse);
        return nurseMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public NurseResponseDto updateNurse(Long nurseId, NurseUpdateDto updateDto) {
        log.info("Updating nurse with id: {}", nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));

        if (updateDto.getNurseCode() != null && !updateDto.getNurseCode().equals(nurse.getNurseCode())) {
            if (nurseRepository.existsByNurseCode(updateDto.getNurseCode())) {
                throw NurseAlreadyExistsException.byNurseCode(updateDto.getNurseCode());
            }
        }
        if (updateDto.getNationalId() != null && !updateDto.getNationalId().equals(nurse.getNationalId())) {
            if (nurseRepository.existsByNationalId(updateDto.getNationalId())) {
                throw NurseAlreadyExistsException.byNationalId(updateDto.getNationalId());
            }
        }

        nurseMapper.updateEntity(nurse, updateDto);
        Nurse updated = nurseRepository.save(nurse);
        log.info("Nurse updated with id: {}", updated.getId());
        return nurseMapper.toResponseDto(updated);
    }

    // ========== Basic Retrieval ==========

    @Override
    @Transactional(readOnly = true)
    public NurseResponseDto getNurseById(Long nurseId) {
        log.debug("Fetching nurse by id: {}", nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    @Transactional(readOnly = true)
    public NurseResponseDto getNurseByUserId(Long userId) {
        log.debug("Fetching nurse by userId: {}", userId);
        Nurse nurse = nurseRepository.findByUserId(userId)
                .orElseThrow(() -> NurseNotFoundException.byUserId(userId));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    public NurseResponseDto getNurseByNurseCode(String nurseCode) {
        log.debug("Fetching nurse by nurseCode: {}", nurseCode);
        Nurse nurse = nurseRepository.findByNurseCode(nurseCode)
                .orElseThrow(() -> NurseNotFoundException.byNurseCode(nurseCode));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    public NurseResponseDto getNurseByNationalId(String nationalId) {
        log.debug("Fetching nurse by nationalId: {}", nationalId);
        Nurse nurse = nurseRepository.findByNationalId(nationalId)
                .orElseThrow(() -> NurseNotFoundException.byNationalId(nationalId));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    public NurseResponseDto getNurseByPhoneNumber(String phoneNumber) {
        log.debug("Fetching nurse by phoneNumber: {}", phoneNumber);
        Nurse nurse = nurseRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NurseNotFoundException.byPhone(phoneNumber));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    public NurseResponseDto getNurseWithShifts(Long nurseId) {
        log.debug("Fetching nurse with shifts, id: {}", nurseId);
        Nurse nurse = nurseRepository.findByIdWithShifts(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        return nurseMapper.toResponseDto(nurse);
    }

    @Override
    public List<NurseResponseDto> getAllNurses() {
        log.debug("Fetching all nurses");
        return nurseRepository.findAll()
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Search & Filter ==========

    @Override
    @Transactional(readOnly = true)
    public List<NurseResponseDto> searchNursesByName(String firstName, String lastName) {
        log.debug("Searching nurses by name: {}, {}", firstName, lastName);
        if (firstName == null && lastName == null) {
            return getAllNurses();
        } else if (lastName == null) {
            return nurseRepository.findByFirstNameContainingIgnoreCase(firstName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else if (firstName == null) {
            return nurseRepository.findByLastNameContainingIgnoreCase(lastName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else {
            return nurseRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<NurseResponseDto> searchActiveNursesByName(String firstName, String lastName) {
        log.debug("Searching activeNurses by name: {}, {}", firstName, lastName);
        if (firstName == null && lastName == null) {
            return getAllNurses();
        } else if (lastName == null) {
            return nurseRepository.findByFirstNameContainingIgnoreCaseAndActiveTrue(firstName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else if (firstName == null) {
            return nurseRepository.findByLastNameContainingIgnoreCaseAndActiveTrue(lastName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else {
            return nurseRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndActiveTrue(firstName, lastName)
                    .stream()
                    .map(nurseMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<NurseResponseDto> getNursesByPosition(NursePosition position) {
        log.debug("Fetching nurses by position: {}", position);
        return nurseRepository.findByPosition(position)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getActiveNursesByPosition(NursePosition position) {
        log.debug("Fetching activeNurses by position: {}", position);
        return nurseRepository.findByPositionAndActiveTrue(position)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getNursesByDepartmentId(Long departmentId) {
        log.debug("Fetching nurses by departmentId: {}", departmentId);
        return nurseRepository.findByDepartmentId(departmentId)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getNursesByExperienceRange(int min, int max) {
        log.debug("Fetching nurses by experience between {} and {}", min, max);
        return nurseRepository.findByYearsOfExperienceBetween(min, max)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getActiveNursesByExperienceRange(int min, int max) {
        log.debug("Fetching activeNurses by experience between {} and {}", min, max);
        return nurseRepository.findByYearsOfExperienceBetweenAndActiveTrue(min, max)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Status Based ==========

    @Override
    public List<NurseResponseDto> getAllActiveNurses() {
        log.debug("Fetching all active nurses");
        return nurseRepository.findAllActive()
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getAllInactiveNurses() {
        log.debug("Fetching all inactive nurses");
        return nurseRepository.findAllInactive()
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NurseResponseDto> getActiveNursesByDepartmentId(Long departmentId) {
        log.debug("Fetching active nurses by departmentId: {}", departmentId);
        return nurseRepository.findActiveNursesByDepartmentId(departmentId)
                .stream()
                .map(nurseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Status Management ==========

    @Override
    @Transactional
    public void activateNurse(Long nurseId) {
        log.info("Activating nurse id: {}", nurseId);
        nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        nurseRepository.activate(nurseId);
    }

    @Override
    @Transactional
    public void deactivateNurse(Long nurseId) {
        log.warn("Deactivating nurse id: {}", nurseId);
        nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        nurseRepository.deactivate(nurseId);
    }

    // ========== Department Assignment ==========

    @Override
    @Transactional
    public void assignDepartment(Long nurseId, Long departmentId) {
        log.info("Assigning department {} to nurse {}", departmentId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        nurse.addDepartment(department);
        nurseRepository.save(nurse);
    }

    @Override
    @Transactional
    public void removeDepartment(Long nurseId, Long departmentId) {
        log.info("Removing department {} from nurse {}", departmentId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.byId(departmentId));
        nurse.removeDepartment(department);
        nurseRepository.save(nurse);
    }

    // ========== Shift Preference Management ==========

    @Override
    @Transactional
    public void addShiftPreference(Long nurseId, Long shiftId) {
        log.info("Adding shift {} to nurse {}", shiftId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> ShiftNotFoundException.byId(shiftId));
        nurse.addShiftPreference(shift);
        nurseRepository.save(nurse);
    }

    @Override
    @Transactional
    public void removeShiftPreference(Long nurseId, Long shiftId) {
        log.info("Removing shift {} from nurse {}", shiftId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> NurseNotFoundException.byId(nurseId));
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> ShiftNotFoundException.byId(shiftId));
        nurse.removeShiftPreference(shift);
        nurseRepository.save(nurse);
    }

    // ========== Bulk Operation ==========

    @Override
    @Transactional
    public void bulkCreateNurses(List<NurseCreateDto> createDtoList) {
        log.info("Bulk creating {} nurses", createDtoList.size());
        Set<String> nurseCodes = new HashSet<>();
        Set<String> nationalIds = new HashSet<>();

        for (NurseCreateDto dto : createDtoList) {
            if (!nurseCodes.add(dto.getNurseCode())) {
                throw NurseAlreadyExistsException.byNurseCode(dto.getNurseCode());
            }
            if (!nationalIds.add(dto.getNationalId())) {
                throw NurseAlreadyExistsException.byNationalId(dto.getNationalId());
            }
        }

        createDtoList.stream()
                .filter(dto -> nurseRepository.existsByNurseCode(dto.getNurseCode()))
                .findFirst()
                .ifPresent(dto -> {
                    throw NurseAlreadyExistsException.byNurseCode(dto.getNurseCode());
                });

        createDtoList.stream()
                .filter(dto -> nurseRepository.existsByNationalId(dto.getNationalId()))
                .findFirst()
                .ifPresent(dto -> {
                    throw NurseAlreadyExistsException.byNationalId(dto.getNationalId());
                });

        List<Nurse> nurses = createDtoList.stream()
                .map(nurseMapper::toEntity)
                .collect(Collectors.toList());

        List<Nurse> saved = nurseRepository.saveAll(nurses);
        log.info("Successfully bulk created {} nurses", saved.size());
    }

    // ========== Statistics ==========

    @Override
    public Long countNursesByPosition(NursePosition position) {
        return nurseRepository.countByPosition(position);
    }

    @Override
    public Long countNursesByDepartmentId(Long departmentId) {
        return nurseRepository.countByDepartmentId(departmentId);
    }

    @Override
    public Long countActiveNurses() {
        return nurseRepository.countActive();
    }

    @Override
    public Long countInactiveNurses() {
        return nurseRepository.countInactive();
    }

    @Override
    public Long countAllNurses() {
        return nurseRepository.count();
    }

    // ========== Validation ==========

    @Override
    public boolean isNurseCodeUnique(String nurseCode) {
        return !nurseRepository.existsByNurseCode(nurseCode);
    }

    @Override
    public boolean isNationalIdUnique(String nationalId) {
        return !nurseRepository.existsByNationalId(nationalId);
    }

    @Override
    public boolean existsNurseByUserId(Long userId) {
        return nurseRepository.existsByUserId(userId);
    }
}