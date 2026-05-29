package hospital.coreservice.service.imp;

import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.exception.common.InvalidSearchParameterException;
import hospital.coreservice.exception.patient.*;
import hospital.coreservice.exception.room.RoomFullException;
import hospital.coreservice.exception.room.RoomNotFoundException;
import hospital.coreservice.mapper.PatientMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of PatientService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final RoomRepository roomRepository;

    // ========== Create ==========

    @Override
    @Transactional
    public PatientResponseDto createPatient(PatientCreateDto createDto) {
        log.info("Creating patient with nationalId: {}", createDto.getNationalId());

        if (patientRepository.existsByNationalId(createDto.getNationalId())) {
            throw new DuplicateNationalIdException(createDto.getNationalId());
        }
        if (patientRepository.existsByPhoneNumber(createDto.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException(createDto.getPhoneNumber());
        }

        Patient patient = patientMapper.toEntity(createDto);
        patient.setStatus(PatientStatus.ACTIVE);
        Patient saved = patientRepository.save(patient);

        log.info("Patient created with id: {}", saved.getId());
        return patientMapper.toResponseDto(saved);
    }

    // ========== Update ==========

    @Override
    @Transactional
    public PatientResponseDto updatePatient(Long id, PatientUpdateDto updateDto) {
        log.info("Updating patient id: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> PatientNotFoundException.byId(id));

        patientMapper.updateEntity(patient, updateDto);
        Patient updated = patientRepository.save(patient);

        log.info("Patient updated id: {}", id);
        return patientMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public PatientResponseDto patchPatient(Long id, Map<String, Object> updates) {
        log.info("Patching patient id: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> PatientNotFoundException.byId(id));

        updates.forEach((key, value) -> {
            if (value != null) {
                switch (key) {
                    case "nationalId":
                        patient.setNationalId((String) value);
                        break;
                    case "firstName":
                        patient.setFirstName((String) value);
                        break;
                    case "lastName":
                        patient.setLastName((String) value);
                        break;
                    case "gender":
                        patient.setGender(Gender.valueOf((String) value));
                        break;
                    case "phoneNumber":
                        if (!value.equals(patient.getPhoneNumber()) &&
                                patientRepository.existsByPhoneNumber((String) value)) {
                            throw new DuplicatePhoneNumberException((String) value);
                        }
                        patient.setPhoneNumber((String) value);
                        break;
                    case "address":
                        patient.setAddress((String) value);
                        break;
                    case "bloodType":
                        patient.setBloodType(BloodType.valueOf((String) value));
                        break;
                    case "insuranceId":
                        patient.setInsuranceId(value != null ? Long.valueOf((String) value) : null);
                        break;
                    case "status":
                        patient.setStatus(PatientStatus.valueOf((String) value));
                        break;
                    case "allergies":
                        patient.setAllergies((String) value);
                        break;
                    default:
                        log.warn("Unknown field: {}", key);
                }
            }
        });

        Patient updated = patientRepository.save(patient);
        log.info("Patient patched id: {}", id);
        return patientMapper.toResponseDto(updated);
    }

    // ========== Delete ==========

    @Override
    @Transactional
    public void deletePatient(Long id) {
        log.warn("Soft deleting patient id: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> PatientNotFoundException.byId(id));

        patientRepository.delete(patient);
        log.info("Patient deleted id: {}", id);
    }

    @Override
    @Transactional
    public void archivePatients(List<Long> patientIds) {
        log.info("Archiving {} patients", patientIds.size());

        List<Long> failedIds = new ArrayList<>();
        patientIds.forEach(id -> {
            if (patientRepository.archivePatient(id) == 0) {
                failedIds.add(id);
            }
        });

        if (!failedIds.isEmpty()) {
            throw new PatientsArchiveException(failedIds);
        }

        log.info("Archived {} patients", patientIds.size());
    }

    // ========== Single Record Retrieval ==========

    @Override
    public PatientResponseDto getPatientById(Long id) {
        log.debug("Fetching patient id: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> PatientNotFoundException.byId(id));

        return patientMapper.toResponseDto(patient);
    }

    @Override
    public PatientResponseDto getPatientByNationalId(String nationalId) {
        log.debug("Fetching patient by nationalId: {}", nationalId);

        Patient patient = patientRepository.findByNationalId(nationalId)
                .orElseThrow(() -> PatientNotFoundException.byNationalId(nationalId));

        return patientMapper.toResponseDto(patient);
    }

    @Override
    public PatientResponseDto getPatientByPhoneNumber(String phoneNumber) {
        log.debug("Fetching patient by phone: {}", phoneNumber);

        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> PatientNotFoundException.byPhoneNumber(phoneNumber));

        return patientMapper.toResponseDto(patient);
    }

    // ========== Multi-Record Retrieval ==========

    @Override
    public List<PatientResponseDto> getAllPatients() {
        log.debug("Fetching all patients");

        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Search by Name ==========

    @Override
    public List<PatientResponseDto> getPatientsByFirstNameContainingIgnoreCase(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidSearchParameterException("firstName");
        }

        log.debug("Searching by firstName: {}", firstName);
        return patientRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDto> getPatientsByLastNameContainingIgnoreCase(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidSearchParameterException("lastName");
        }

        log.debug("Searching by lastName: {}", lastName);
        return patientRepository.findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDto> getPatientsByFirstNameAndLastName(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidSearchParameterException("firstName");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidSearchParameterException("lastName");
        }

        log.debug("Searching by firstName: {} and lastName: {}", firstName, lastName);
        return patientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Advanced Search ==========

    @Override
    public List<PatientResponseDto> searchPatients(String nationalId, String firstName, String lastName, PatientStatus status) {
        log.debug("Advanced search - nationalId: {}, firstName: {}, lastName: {}, status: {}",
                nationalId, firstName, lastName, status);

        return patientRepository.searchPatients(nationalId, firstName, lastName, status)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Filtering ==========

    @Override
    public List<PatientResponseDto> getPatientsByGender(Gender gender) {
        log.debug("Filtering by gender: {}", gender);
        return patientRepository.findByGender(gender)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDto> getPatientsByBloodType(BloodType bloodType) {
        log.debug("Filtering by bloodType: {}", bloodType);
        return patientRepository.findByBloodType(bloodType)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDto> getPatientsByStatus(PatientStatus status) {
        log.debug("Filtering by status: {}", status);
        return patientRepository.findByStatus(status)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDto> getPatientsByStatus(PatientStatus status, Pageable pageable) {
        log.debug("Filtering by status: {} with pagination", status);
        return patientRepository.findByStatus(status, pageable)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Date Range ==========

    @Override
    public List<PatientResponseDto> getPatientsByBirthDateBetween(LocalDate start, LocalDate end) {
        log.debug("Filtering by birth date between {} and {}", start, end);
        return patientRepository.findByBirthDateBetween(start, end)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Room Assignment ==========

    @Override
    public List<PatientResponseDto> getPatientsByCurrentRoomId(Long roomId) {
        log.debug("Fetching patients in room: {}", roomId);
        return patientRepository.findByCurrentRoomId(roomId)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignRoom(Long patientId, Long roomId) {
        log.info("Assigning patient {} to room {}", patientId, roomId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> PatientNotFoundException.byId(patientId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));

        if (room.getCurrentPatientList().size() >= room.getCapacity()) {
            throw new RoomFullException(roomId, room.getCapacity());
        }

        if (patient.getCurrentRoom() != null) {
            throw new PatientAlreadyHasRoomException(patientId, patient.getCurrentRoom().getId());
        }

        patient.setCurrentRoom(room);
        patientRepository.save(patient);

        room.addPatient(patient);
        roomRepository.save(room);

        log.info("Patient {} assigned to room {}", patientId, roomId);
    }

    @Override
    @Transactional
    public void unassignRoom(Long patientId) {
        log.info("Removing patient {} from room", patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> PatientNotFoundException.byId(patientId));

        if (patient.getCurrentRoom() == null) {
            throw new PatientNotInRoomException(patientId);
        }

        Room currentRoom = patient.getCurrentRoom();

        patient.setCurrentRoom(null);
        currentRoom.removePatient(patient);

        patientRepository.save(patient);
        roomRepository.save(currentRoom);

        log.info("Patient {} removed from room {}", patientId, currentRoom.getId());
    }

    // ========== Counting ==========

    @Override
    public Long countPatientsByStatus(PatientStatus status) {
        return patientRepository.countByStatus(status);
    }

    @Override
    public Long countPatientsByGender(Gender gender) {
        return patientRepository.countByGender(gender);
    }

    @Override
    public Long countPatientsByBloodType(BloodType bloodType) {
        return patientRepository.countByBloodType(bloodType);
    }

    @Override
    public Long countActivePatients() {
        return patientRepository.countByStatus(PatientStatus.ACTIVE);
    }

    // ========== Existence Checks ==========

    @Override
    public boolean existsPatientByNationalId(String nationalId) {
        return patientRepository.existsByNationalId(nationalId);
    }

    @Override
    public boolean existsPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.existsByPhoneNumber(phoneNumber);
    }

    // ========== Status Management ==========

    @Override
    @Transactional
    public void activatePatient(Long id) {
        log.info("Activating patient id: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> PatientNotFoundException.byId(id));

        if (patient.getStatus() == PatientStatus.ACTIVE) {
            throw new PatientAlreadyActiveException(id);
        }

        if (patientRepository.activatePatient(id) == 0) {
            throw new PatientActivationFailedException(id);
        }

        log.info("Patient activated id: {}", id);
    }
}