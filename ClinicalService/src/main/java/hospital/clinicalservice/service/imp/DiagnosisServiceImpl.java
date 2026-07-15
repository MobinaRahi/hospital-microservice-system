package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.diagnosis.DiagnosisCreateDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisUpdateDto;
import hospital.clinicalservice.exception.diagnosis.DiagnosisNotFoundException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.mapper.DiagnosisMapper;
import hospital.clinicalservice.model.Diagnosis;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.repository.DiagnosisRepository;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.service.DiagnosisService;
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
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final DiagnosisMapper diagnosisMapper;
    private final EncounterRepository encounterRepository;

    @Override
    @Transactional
    public DiagnosisResponseDto createDiagnosis(DiagnosisCreateDto createDto) {
        log.info("Creating diagnosis for encounter: {}", createDto.getEncounterId());
        Encounter encounter = encounterRepository.findNotDeletedById(createDto.getEncounterId())
                .orElseThrow(() -> EncounterNotFoundException.byId(createDto.getEncounterId()));
        Diagnosis diagnosis = diagnosisMapper.toEntity(createDto);
        diagnosis.setEncounter(encounter);
        Diagnosis saved = diagnosisRepository.save(diagnosis);
        log.info("Diagnosis created with id: {}", saved.getId());
        return diagnosisMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public DiagnosisResponseDto updateDiagnosis(Long id, DiagnosisUpdateDto updateDto) {
        log.info("Updating diagnosis id: {}", id);
        Diagnosis diagnosis = diagnosisRepository.findNotDeletedById(id)
                .orElseThrow(() -> DiagnosisNotFoundException.byId(id));
        diagnosisMapper.updateEntity(diagnosis, updateDto);
        Diagnosis updated = diagnosisRepository.save(diagnosis);
        log.info("Diagnosis updated id: {}", id);
        return diagnosisMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteDiagnosis(Long id) {
        log.info("Deleting diagnosis id: {}", id);
        diagnosisRepository.softDeleteById(id, LocalDateTime.now());
        log.info("Diagnosis deleted id: {}", id);
    }

    @Override
    public DiagnosisResponseDto getDiagnosisById(Long id) {
        log.debug("Fetching diagnosis by id: {}", id);
        Diagnosis diagnosis = diagnosisRepository.findNotDeletedById(id)
                .orElseThrow(() -> DiagnosisNotFoundException.byId(id));
        return diagnosisMapper.toResponseDto(diagnosis);
    }

    @Override
    public List<DiagnosisResponseDto> getDiagnosesByEncounterId(Long encounterId) {
        log.debug("Fetching diagnoses by encounterId: {}", encounterId);
        return diagnosisRepository.findByEncounterId(encounterId)
                .stream()
                .map(diagnosisMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiagnosisResponseDto> getPrimaryDiagnosesByEncounterId(Long encounterId) {
        log.debug("Fetching primary diagnoses by encounterId: {}", encounterId);
        return diagnosisRepository.findByEncounterIdAndPrimary(encounterId, true)
                .stream()
                .map(diagnosisMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiagnosisResponseDto> getDiagnosesByIcd10Code(String icd10Code) {
        log.debug("Fetching diagnoses by ICD-10 code: {}", icd10Code);
        return diagnosisRepository.findByIcd10Code(icd10Code)
                .stream()
                .map(diagnosisMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiagnosisResponseDto> searchDiagnoses(String query) {
        log.debug("Searching diagnoses by query: {}", query);
        return diagnosisRepository.searchDiagnoses(query)
                .stream()
                .map(diagnosisMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiagnosisResponseDto> getDiagnosesByPatientId(Long patientId) {
        log.debug("Fetching diagnoses by patientId: {}", patientId);
        return diagnosisRepository.findByPatientId(patientId)
                .stream()
                .map(diagnosisMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countDiagnosesByEncounterId(Long encounterId) {
        return diagnosisRepository.countByEncounterId(encounterId);
    }

    @Override
    public Long countDiagnosesByPatientId(Long patientId) {
        return diagnosisRepository.countByPatientId(patientId);
    }
}
