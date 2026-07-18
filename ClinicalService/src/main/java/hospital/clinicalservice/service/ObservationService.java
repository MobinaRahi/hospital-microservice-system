package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.observation.ObservationCreateDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.observation.ObservationUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Observation management.
 * Handles CRUD operations for vital signs using LOINC codes.
 *
 * @author Mobina
 */
public interface ObservationService {

    ObservationResponseDto createObservation(ObservationCreateDto createDto);

    ObservationResponseDto updateObservation(Long id, ObservationUpdateDto updateDto);

    void deleteObservation(Long id);

    ObservationResponseDto getObservationById(Long id);

    List<ObservationResponseDto> getObservationsByEncounterId(Long encounterId);

    List<ObservationResponseDto> getObservationsByPatientId(Long patientId);

    List<ObservationResponseDto> getObservationsByPatientIdAndLoincCode(Long patientId, String loincCode);

    List<ObservationResponseDto> getAbnormalObservationsByPatientId(Long patientId);

    ObservationResponseDto getLatestObservationByPatientAndLoinc(Long patientId, String loincCode);

    List<ObservationResponseDto> getObservationsByDateRange(LocalDateTime start, LocalDateTime end);

    Long countObservationsByPatientId(Long patientId);

    Long countAbnormalObservationsByPatientId(Long patientId);
}
