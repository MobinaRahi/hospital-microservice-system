package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.observation.ObservationCreateDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.observation.ObservationUpdateDto;
import hospital.clinicalservice.exception.observation.ObservationNotFoundException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.mapper.ObservationMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.Observation;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.repository.ObservationRepository;
import hospital.clinicalservice.service.ObservationService;
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
public class ObservationServiceImpl implements ObservationService {

    private final ObservationRepository observationRepository;
    private final ObservationMapper observationMapper;
    private final EncounterRepository encounterRepository;

    @Override
    @Transactional
    public ObservationResponseDto createObservation(ObservationCreateDto createDto) {
        log.info("Creating observation for patient: {}", createDto.getPatientId());
        Observation observation = observationMapper.toEntity(createDto);
        if (createDto.getEncounterId() != null) {
            Encounter encounter = encounterRepository.findNotDeletedById(createDto.getEncounterId())
                    .orElseThrow(() -> EncounterNotFoundException.byId(createDto.getEncounterId()));
            observation.setEncounter(encounter);
        }
        Observation saved = observationRepository.save(observation);
        log.info("Observation created with id: {}", saved.getId());
        return observationMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public ObservationResponseDto updateObservation(Long id, ObservationUpdateDto updateDto) {
        log.info("Updating observation id: {}", id);
        Observation observation = observationRepository.findNotDeletedById(id)
                .orElseThrow(() -> ObservationNotFoundException.byId(id));
        observationMapper.updateEntity(observation, updateDto);
        Observation updated = observationRepository.save(observation);
        log.info("Observation updated id: {}", id);
        return observationMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteObservation(Long id) {
        log.info("Deleting observation id: {}", id);
        observationRepository.softDeleteById(id,LocalDateTime.now());
        log.info("Observation deleted id: {}", id);
    }

    @Override
    public ObservationResponseDto getObservationById(Long id) {
        log.debug("Fetching observation by id: {}", id);
        Observation observation = observationRepository.findNotDeletedById(id)
                .orElseThrow(() -> ObservationNotFoundException.byId(id));
        return observationMapper.toResponseDto(observation);
    }

    @Override
    public List<ObservationResponseDto> getObservationsByEncounterId(Long encounterId) {
        log.debug("Fetching observations by encounterId: {}", encounterId);
        return observationRepository.findByEncounterId(encounterId)
                .stream()
                .map(observationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObservationResponseDto> getObservationsByPatientId(Long patientId) {
        log.debug("Fetching observations by patientId: {}", patientId);
        return observationRepository.findByPatientIdOrderByObservedAtDesc(patientId)
                .stream()
                .map(observationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObservationResponseDto> getObservationsByPatientIdAndLoincCode(Long patientId, String loincCode) {
        log.debug("Fetching observations by patientId: {} and loincCode: {}", patientId, loincCode);
        return observationRepository.findByPatientIdAndLoincCode(patientId, loincCode)
                .stream()
                .map(observationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObservationResponseDto> getAbnormalObservationsByPatientId(Long patientId) {
        log.debug("Fetching abnormal observations by patientId: {}", patientId);
        return observationRepository.findAbnormalByPatientId(patientId)
                .stream()
                .map(observationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ObservationResponseDto getLatestObservationByPatientAndLoinc(Long patientId, String loincCode) {
        log.debug("Fetching latest observation for patient: {} loincCode: {}", patientId, loincCode);
        Observation observation = observationRepository.findLatestByPatientAndLoinc(patientId, loincCode);
        if (observation == null) {
            throw ObservationNotFoundException.byPatientIdAndLoinc(patientId, loincCode);
        }
        return observationMapper.toResponseDto(observation);
    }

    @Override
    public List<ObservationResponseDto> getObservationsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching observations between {} and {}", start, end);
        return observationRepository.findByObservedAtBetween(start, end)
                .stream()
                .map(observationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countObservationsByPatientId(Long patientId) {
        return observationRepository.countByPatientId(patientId);
    }

    @Override
    public Long countAbnormalObservationsByPatientId(Long patientId) {
        return observationRepository.countAbnormalByPatientId(patientId);
    }
}
