package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.triage.TriageCreateDto;
import hospital.clinicalservice.dto.triage.TriageResponseDto;
import hospital.clinicalservice.dto.triage.TriageUpdateDto;
import hospital.clinicalservice.exception.triage.TriageNotFoundException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.mapper.TriageMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.Triage;
import hospital.clinicalservice.model.enums.TriageLevel;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.repository.TriageRepository;
import hospital.clinicalservice.service.TriageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TriageService.
 * Manages emergency triage assessments.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TriageServiceImpl implements TriageService {

    private final TriageRepository triageRepository;
    private final TriageMapper triageMapper;
    private final EncounterRepository encounterRepository;

    @Override
    @Transactional
    public TriageResponseDto createTriage(TriageCreateDto createDto) {
        log.info("Creating triage for patient: {} level: {}", createDto.getPatientId(), createDto.getLevel());
        Triage triage = triageMapper.toEntity(createDto);
        if (createDto.getEncounterId() != null) {
            Encounter encounter = encounterRepository.findNotDeletedById(createDto.getEncounterId())
                    .orElseThrow(() -> EncounterNotFoundException.byId(createDto.getEncounterId()));
            triage.setEncounter(encounter);
        }
        Triage saved = triageRepository.save(triage);
        log.info("Triage created with id: {} level: {}", saved.getId(), saved.getLevel());
        return triageMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public TriageResponseDto updateTriage(Long id, TriageUpdateDto updateDto) {
        log.info("Updating triage id: {}", id);
        Triage triage = triageRepository.findNotDeletedById(id)
                .orElseThrow(() -> TriageNotFoundException.byId(id));
        triageMapper.updateEntity(triage, updateDto);
        Triage updated = triageRepository.save(triage);
        return triageMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteTriage(Long id) {
        log.info("Deleting triage id: {}", id);
        triageRepository.softDeleteById(id, LocalDateTime.now());
    }

    @Override
    public TriageResponseDto getTriageById(Long id) {
        Triage triage = triageRepository.findNotDeletedById(id)
                .orElseThrow(() -> TriageNotFoundException.byId(id));
        return triageMapper.toResponseDto(triage);
    }

    @Override
    public List<TriageResponseDto> getTriagesByEncounterId(Long encounterId) {
        return triageRepository.findByEncounterId(encounterId)
                .stream().map(triageMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<TriageResponseDto> getTriagesByPatientId(Long patientId) {
        return triageRepository.findByPatientIdOrderByTriagedAtDesc(patientId)
                .stream().map(triageMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public TriageResponseDto getLatestTriageByPatientId(Long patientId) {
        Triage triage = triageRepository.findFirstByPatientIdOrderByTriagedAtDesc(patientId);
        if (triage == null) throw TriageNotFoundException.byPatientId(patientId);
        return triageMapper.toResponseDto(triage);
    }

    @Override
    public List<TriageResponseDto> getTriagesByLevel(TriageLevel level) {
        return triageRepository.findByLevel(level)
                .stream().map(triageMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<TriageResponseDto> getCriticalCases() {
        return triageRepository.findCriticalCases()
                .stream().map(triageMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public Long countTriagesByPatientId(Long patientId) {
        return triageRepository.countByPatientId(patientId);
    }

    @Override
    public Long countTriagesByLevel(TriageLevel level) {
        return triageRepository.countByLevel(level);
    }

    @Override
    public Long countCriticalCases() {
        return triageRepository.countCriticalCases();
    }
}
