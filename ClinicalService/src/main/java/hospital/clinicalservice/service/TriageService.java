package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.triage.TriageCreateDto;
import hospital.clinicalservice.dto.triage.TriageResponseDto;
import hospital.clinicalservice.dto.triage.TriageUpdateDto;
import hospital.clinicalservice.model.enums.TriageLevel;

import java.util.List;

/**
 * Service interface for Triage management.
 * Handles CRUD and severity tracking for emergency triage.
 *
 * @author Mobina
 */
public interface TriageService {

    TriageResponseDto createTriage(TriageCreateDto createDto);

    TriageResponseDto updateTriage(Long id, TriageUpdateDto updateDto);

    void deleteTriage(Long id);

    TriageResponseDto getTriageById(Long id);

    List<TriageResponseDto> getTriagesByEncounterId(Long encounterId);

    List<TriageResponseDto> getTriagesByPatientId(Long patientId);

    TriageResponseDto getLatestTriageByPatientId(Long patientId);

    List<TriageResponseDto> getTriagesByLevel(TriageLevel level);

    List<TriageResponseDto> getCriticalCases();

    Long countTriagesByPatientId(Long patientId);

    Long countTriagesByLevel(TriageLevel level);

    Long countCriticalCases();
}
