package hospital.billingservice.service.impl;

import hospital.billingservice.dto.insurancemanagement.InsuranceManagementCreateDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementUpdateDto;
import hospital.billingservice.exception.insurance.DuplicateInsuranceCodeException;
import hospital.billingservice.exception.insurance.InsuranceManagementNotFoundException;
import hospital.billingservice.mapper.InsuranceManagementMapper;
import hospital.billingservice.model.InsuranceManagement;
import hospital.billingservice.repository.InsuranceManagementRepository;
import hospital.billingservice.service.InsuranceManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link InsuranceManagementService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InsuranceManagementServiceImpl implements InsuranceManagementService {

    private final InsuranceManagementRepository insuranceRepository;
    private final InsuranceManagementMapper insuranceMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InsuranceManagementResponseDto createInsurance(InsuranceManagementCreateDto dto) {
        log.info("Creating insurance plan: {}", dto.getName());

        // Validate unique code
        if (insuranceRepository.existsByCode(dto.getCode())) {
            throw new DuplicateInsuranceCodeException(dto.getCode());
        }

        // Map DTO to entity
        InsuranceManagement insurance = insuranceMapper.toEntity(dto);
        insurance.setIsActive(true);

        // Save and return
        InsuranceManagement saved = insuranceRepository.save(insurance);
        log.info("Insurance plan created with id: {}", saved.getId());

        return insuranceMapper.toResponseDto(saved);
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public InsuranceManagementResponseDto getInsuranceById(Long id) {
        log.debug("Fetching insurance by id: {}", id);

        InsuranceManagement insurance = insuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InsuranceManagementNotFoundException.byId(id));

        return insuranceMapper.toResponseDto(insurance);
    }

    @Override
    @Transactional(readOnly = true)
    public InsuranceManagementResponseDto getInsuranceByCode(String code) {
        log.debug("Fetching insurance by code: {}", code);

        InsuranceManagement insurance = insuranceRepository.findByCode(code)
                .orElseThrow(() -> InsuranceManagementNotFoundException.byCode(code));

        return insuranceMapper.toResponseDto(insurance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceManagementResponseDto> getAllActiveInsurances() {
        log.debug("Fetching all active insurances");

        List<InsuranceManagement> insurances = insuranceRepository.findByIsActiveTrue();
        return insuranceMapper.toResponseDtoList(insurances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceManagementResponseDto> getAllInsurances() {
        log.debug("Fetching all insurances");

        List<InsuranceManagement> insurances = insuranceRepository.findAllNotDeleted();
        return insuranceMapper.toResponseDtoList(insurances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceManagementResponseDto> searchByName(String name) {
        log.debug("Searching insurances by name: {}", name);

        List<InsuranceManagement> insurances = insuranceRepository.findByNameContainingIgnoreCase(name);
        return insuranceMapper.toResponseDtoList(insurances);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public InsuranceManagementResponseDto updateInsurance(Long id, InsuranceManagementUpdateDto dto) {
        log.info("Updating insurance id: {}", id);

        InsuranceManagement insurance = insuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InsuranceManagementNotFoundException.byId(id));

        // Map update DTO to entity
        insuranceMapper.updateEntity(dto, insurance);

        InsuranceManagement saved = insuranceRepository.save(insurance);
        log.info("Insurance updated id: {}", saved.getId());

        return insuranceMapper.toResponseDto(saved);
    }

    @Override
    public InsuranceManagementResponseDto toggleActive(Long id) {
        log.info("Toggling active status for insurance id: {}", id);

        InsuranceManagement insurance = insuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InsuranceManagementNotFoundException.byId(id));

        insurance.setIsActive(!insurance.getIsActive());
        InsuranceManagement saved = insuranceRepository.save(insurance);

        return insuranceMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deleteInsurance(Long id) {
        log.info("Soft-deleting insurance id: {}", id);

        InsuranceManagement insurance = insuranceRepository.findNotDeletedById(id)
                .orElseThrow(() -> InsuranceManagementNotFoundException.byId(id));

        insurance.softDelete(null);
        insuranceRepository.save(insurance);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return insuranceRepository.existsByCode(code);
    }
}
