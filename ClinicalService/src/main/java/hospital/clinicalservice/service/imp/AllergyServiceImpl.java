package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.allergy.AllergyCreateDto;
import hospital.clinicalservice.dto.allergy.AllergyResponseDto;
import hospital.clinicalservice.dto.allergy.AllergyUpdateDto;
import hospital.clinicalservice.exception.allergy.AllergyNotFoundException;
import hospital.clinicalservice.mapper.AllergyMapper;
import hospital.clinicalservice.model.Allergy;
import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import hospital.clinicalservice.repository.AllergyRepository;
import hospital.clinicalservice.service.AllergyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AllergyService.
 * Manages patient allergies (drug, food, environmental).
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;

    @Override
    @Transactional
    public AllergyResponseDto createAllergy(AllergyCreateDto createDto) {
        log.info("Creating allergy for patient: {} allergen: {}", createDto.getPatientId(), createDto.getAllergenName());
        Allergy allergy = allergyMapper.toEntity(createDto);
        allergy.setActive(true);
        Allergy saved = allergyRepository.save(allergy);
        log.info("Allergy created with id: {}", saved.getId());
        return allergyMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public AllergyResponseDto updateAllergy(Long id, AllergyUpdateDto updateDto) {
        log.info("Updating allergy id: {}", id);
        Allergy allergy = allergyRepository.findNotDeletedById(id)
                .orElseThrow(() -> AllergyNotFoundException.byId(id));
        allergyMapper.updateEntity(allergy, updateDto);
        Allergy updated = allergyRepository.save(allergy);
        return allergyMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteAllergy(Long id) {
        log.info("Deleting allergy id: {}", id);
        allergyRepository.softDeleteById(id, LocalDateTime.now());
    }

    @Override
    public AllergyResponseDto getAllergyById(Long id) {
        Allergy allergy = allergyRepository.findNotDeletedById(id)
                .orElseThrow(() -> AllergyNotFoundException.byId(id));
        return allergyMapper.toResponseDto(allergy);
    }

    @Override
    public List<AllergyResponseDto> getAllergiesByPatientId(Long patientId) {
        return allergyRepository.findByPatientId(patientId)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> getActiveAllergiesByPatientId(Long patientId) {
        return allergyRepository.findActiveByPatientId(patientId)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> getAllergiesByType(AllergyType type) {
        return allergyRepository.findByType(type)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> getAllergiesByPatientIdAndType(Long patientId, AllergyType type) {
        return allergyRepository.findByPatientIdAndType(patientId, type)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> getAllergiesBySeverity(AllergySeverity severity) {
        return allergyRepository.findBySeverity(severity)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> getSevereAllergiesByPatientId(Long patientId) {
        return allergyRepository.findSevereByPatientId(patientId)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<AllergyResponseDto> searchAllergiesByAllergenName(String query) {
        return allergyRepository.searchByAllergenName(query)
                .stream().map(allergyMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public Long countAllergiesByPatientId(Long patientId) {
        return allergyRepository.countByPatientId(patientId);
    }

    @Override
    public Long countActiveAllergiesByPatientId(Long patientId) {
        return allergyRepository.countByPatientIdAndActive(patientId, true);
    }

    @Override
    public Long countSevereAllergiesByPatientId(Long patientId) {
        return allergyRepository.countSevereByPatientId(patientId);
    }
}
