package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.exception.encounter.InvalidEncounterStateException;
import hospital.clinicalservice.mapper.EncounterMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.service.EncounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final EncounterMapper encounterMapper;

    @Override
    @Transactional
    public EncounterResponseDto createEncounter(EncounterCreateDto createDto) {
        log.info("Creating encounter for patient: {} by doctor: {}", createDto.getPatientId(), createDto.getDoctorId());
        Encounter encounter = encounterMapper.toEntity(createDto);
        encounter.setEncounterDate(LocalDateTime.now());
        encounter.setStatus(EncounterStatus.IN_PROGRESS);
        Encounter saved = encounterRepository.save(encounter);
        log.info("Encounter created with id: {}", saved.getId());
        return encounterMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public EncounterResponseDto updateEncounter(Long id, EncounterUpdateDto updateDto) {
        log.info("Updating encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        encounterMapper.updateEntity(encounter, updateDto);
        Encounter updated = encounterRepository.save(encounter);
        log.info("Encounter updated id: {}", id);
        return encounterMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void completeEncounter(Long id) {
        log.info("Completing encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        if (encounter.getStatus() == EncounterStatus.COMPLETED) {
            throw InvalidEncounterStateException.alreadyCompleted(id);
        }
        if (encounter.getStatus() == EncounterStatus.CANCELLED) {
            throw InvalidEncounterStateException.cannotComplete(id, "CANCELLED");
        }
        encounter.setStatus(EncounterStatus.COMPLETED);
        encounterRepository.save(encounter);
        log.info("Encounter completed id: {}", id);
    }

    @Override
    @Transactional
    public void cancelEncounter(Long id) {
        log.info("Cancelling encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        if (encounter.getStatus() == EncounterStatus.CANCELLED) {
            throw InvalidEncounterStateException.alreadyCancelled(id);
        }
        encounter.setStatus(EncounterStatus.CANCELLED);
        encounterRepository.save(encounter);
        log.info("Encounter cancelled id: {}", id);
    }

    @Override
    public EncounterResponseDto getEncounterById(Long id) {
        log.debug("Fetching encounter by id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        return encounterMapper.toResponseDto(encounter);
    }

    @Override
    public List<EncounterResponseDto> getEncountersByPatientId(Long patientId) {
        log.debug("Fetching encounters by patientId: {}", patientId);
        return encounterRepository.findByPatientIdOrderByEncounterDateDesc(patientId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getEncountersByDoctorId(Long doctorId) {
        log.debug("Fetching encounters by doctorId: {}", doctorId);
        return encounterRepository.findByDoctorIdOrderByEncounterDateDesc(doctorId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getEncountersByStatus(EncounterStatus status) {
        log.debug("Fetching encounters by status: {}", status);
        return encounterRepository.findByStatus(status)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getEncountersByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching encounters between {} and {}", start, end);
        return encounterRepository.findByEncounterDateBetween(start, end)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getTodayEncounters() {
        log.debug("Fetching today's encounters");
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return encounterRepository.findTodayEncounters(start, end)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getEncountersByAppointmentId(Long appointmentId) {
        log.debug("Fetching encounters by appointmentId: {}", appointmentId);
        return encounterRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EncounterResponseDto> getEncountersByDepartmentId(Long departmentId) {
        log.debug("Fetching encounters by departmentId: {}", departmentId);
        return encounterRepository.findByDepartmentId(departmentId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countEncountersByPatientId(Long patientId) {
        return encounterRepository.countByPatientId(patientId);
    }

    @Override
    public Long countEncountersByDoctorId(Long doctorId) {
        return encounterRepository.countByDoctorId(doctorId);
    }

    @Override
    public Long countEncountersByStatus(EncounterStatus status) {
        return encounterRepository.countByStatus(status);
    }

    @Override
    public Long countEncountersByPatientIdAndStatus(Long patientId, EncounterStatus status) {
        return encounterRepository.countByPatientIdAndStatus(patientId, status);
    }
}
