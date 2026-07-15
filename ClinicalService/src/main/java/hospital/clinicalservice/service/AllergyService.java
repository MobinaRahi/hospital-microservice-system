package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.allergy.AllergyCreateDto;
import hospital.clinicalservice.dto.allergy.AllergyResponseDto;
import hospital.clinicalservice.dto.allergy.AllergyUpdateDto;
import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;

import java.util.List;

public interface AllergyService {

    AllergyResponseDto createAllergy(AllergyCreateDto createDto);

    AllergyResponseDto updateAllergy(Long id, AllergyUpdateDto updateDto);

    void deleteAllergy(Long id);

    AllergyResponseDto getAllergyById(Long id);

    List<AllergyResponseDto> getAllergiesByPatientId(Long patientId);

    List<AllergyResponseDto> getActiveAllergiesByPatientId(Long patientId);

    List<AllergyResponseDto> getAllergiesByType(AllergyType type);

    List<AllergyResponseDto> getAllergiesByPatientIdAndType(Long patientId, AllergyType type);

    List<AllergyResponseDto> getAllergiesBySeverity(AllergySeverity severity);

    List<AllergyResponseDto> getSevereAllergiesByPatientId(Long patientId);

    List<AllergyResponseDto> searchAllergiesByAllergenName(String query);

    Long countAllergiesByPatientId(Long patientId);

    Long countActiveAllergiesByPatientId(Long patientId);

    Long countSevereAllergiesByPatientId(Long patientId);
}
