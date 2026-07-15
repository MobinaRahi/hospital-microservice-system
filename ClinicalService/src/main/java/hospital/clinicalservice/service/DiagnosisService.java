package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.diagnosis.DiagnosisCreateDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.diagnosis.DiagnosisUpdateDto;

import java.util.List;

public interface DiagnosisService {

    DiagnosisResponseDto createDiagnosis(DiagnosisCreateDto createDto);

    DiagnosisResponseDto updateDiagnosis(Long id, DiagnosisUpdateDto updateDto);

    void deleteDiagnosis(Long id);

    DiagnosisResponseDto getDiagnosisById(Long id);

    List<DiagnosisResponseDto> getDiagnosesByEncounterId(Long encounterId);

    List<DiagnosisResponseDto> getPrimaryDiagnosesByEncounterId(Long encounterId);

    List<DiagnosisResponseDto> getDiagnosesByIcd10Code(String icd10Code);

    List<DiagnosisResponseDto> searchDiagnoses(String query);

    List<DiagnosisResponseDto> getDiagnosesByPatientId(Long patientId);

    Long countDiagnosesByEncounterId(Long encounterId);

    Long countDiagnosesByPatientId(Long patientId);
}
