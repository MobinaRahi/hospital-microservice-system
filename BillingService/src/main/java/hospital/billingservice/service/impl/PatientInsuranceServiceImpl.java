package hospital.billingservice.service.impl;

import hospital.billingservice.dto.patientinsurance.PatientInsuranceCreateDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceResponseDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceUpdateDto;
import hospital.billingservice.exception.patientinsurance.DuplicatePolicyNumberException;
import hospital.billingservice.exception.patientinsurance.PatientInsuranceNotFoundException;
import hospital.billingservice.mapper.PatientInsuranceMapper;
import hospital.billingservice.model.PatientInsurance;
import hospital.billingservice.repository.PatientInsuranceRepository;
import hospital.billingservice.service.PatientInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PatientInsuranceService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientInsuranceServiceImpl implements PatientInsuranceService {

    private final PatientInsuranceRepository patientInsuranceRepository;
    private final PatientInsuranceMapper patientInsuranceMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PatientInsuranceResponseDto createPatientInsurance(PatientInsuranceCreateDto dto) {
        log.info("Creating patient insurance for patient: {}", dto.getPatientId());

        // Validate unique policy number
        if (patientInsuranceRepository.existsByPolicyNumber(dto.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(dto.getPolicyNumber());
        }

        // Map DTO to entity
        PatientInsurance patientInsurance = patientInsuranceMapper.toEntity(dto);

        // Save and return
        PatientInsurance saved = patientInsuranceRepository.save(patientInsurance);
        log.info("Patient insurance created with id: {}", saved.getId());

        return patientInsuranceMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PatientInsuranceResponseDto getPatientInsuranceById(Long id) {
        log.debug("Fetching patient insurance by id: {}", id);

        PatientInsurance patientInsurance = patientInsuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> PatientInsuranceNotFoundException.byId(id));

        return patientInsuranceMapper.toResponseDto(patientInsurance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientInsuranceResponseDto> getInsurancesByPatient(Long patientId) {
        log.debug("Fetching insurances for patient: {}", patientId);

        List<PatientInsurance> patientInsurances = patientInsuranceRepository.findByPatientId(patientId);
        return patientInsuranceMapper.toResponseDtoList(patientInsurances);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientInsuranceResponseDto getPrimaryInsurance(Long patientId) {
        log.debug("Fetching primary insurance for patient: {}", patientId);

        PatientInsurance patientInsurance = patientInsuranceRepository.findByPatientIdAndIsPrimaryTrue(patientId)
                .orElseThrow(() -> PatientInsuranceNotFoundException.byPolicyNumber("Primary not found for patient " + patientId));

        return patientInsuranceMapper.toResponseDto(patientInsurance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientInsuranceResponseDto> getValidInsurances(Long patientId) {
        log.debug("Fetching valid insurances for patient: {}", patientId);

        List<PatientInsurance> patientInsurances = patientInsuranceRepository.findByPatientIdAndExpiryDateIsNull(patientId);
        return patientInsuranceMapper.toResponseDtoList(patientInsurances);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public PatientInsuranceResponseDto updatePatientInsurance(Long id, PatientInsuranceUpdateDto dto) {
        log.info("Updating patient insurance id: {}", id);

        PatientInsurance patientInsurance = patientInsuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> PatientInsuranceNotFoundException.byId(id));

        // Map update DTO to entity
        patientInsuranceMapper.updateEntity(dto, patientInsurance);

        PatientInsurance saved = patientInsuranceRepository.save(patientInsurance);
        log.info("Patient insurance updated id: {}", saved.getId());

        return patientInsuranceMapper.toResponseDto(saved);
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deletePatientInsurance(Long id) {
        log.info("Soft-deleting patient insurance id: {}", id);

        PatientInsurance patientInsurance = patientInsuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> PatientInsuranceNotFoundException.byId(id));

        patientInsurance.softDelete(null);
        patientInsuranceRepository.save(patientInsurance);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean policyNumberExists(String policyNumber) {
        return patientInsuranceRepository.existsByPolicyNumber(policyNumber);
    }
}
