package hospital.tenantservice.service.impl;

import hospital.tenantservice.dto.tenantfeature.TenantFeatureCreateDto;
import hospital.tenantservice.dto.tenantfeature.TenantFeatureResponseDto;
import hospital.tenantservice.exception.tenantfeature.DuplicateTenantFeatureException;
import hospital.tenantservice.exception.tenantfeature.TenantFeatureNotFoundException;
import hospital.tenantservice.mapper.TenantFeatureMapper;
import hospital.tenantservice.model.TenantFeature;
import hospital.tenantservice.repository.TenantFeatureRepository;
import hospital.tenantservice.service.TenantFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link TenantFeatureService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each tenant can have multiple features enabled/disabled</li>
 *   <li>Features are tied to subscription plan (plan features) or sold as add-ons</li>
 *   <li>Add-on features can have additional monthly cost</li>
 *   <li>Features can be enabled/disabled without deleting the record</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TenantFeatureServiceImpl implements TenantFeatureService {

    private final TenantFeatureRepository tenantFeatureRepository;
    private final TenantFeatureMapper tenantFeatureMapper;

    @Override
    public TenantFeatureResponseDto addFeature(TenantFeatureCreateDto dto) {
        log.info("Adding feature {} to tenant {}", dto.getFeatureCode(), dto.getTenantId());

        // Check if feature already exists for tenant
        if (tenantFeatureRepository.existsByTenantIdAndFeatureCode(dto.getTenantId(), dto.getFeatureCode())) {
            throw DuplicateTenantFeatureException.forTenant(dto.getTenantId(), dto.getFeatureCode());
        }

        // Map DTO to entity
        TenantFeature feature = tenantFeatureMapper.toEntity(dto);
        feature.setTenantId(dto.getTenantId());
        feature.setIsActive(true);

        // Save
        TenantFeature saved = tenantFeatureRepository.save(feature);
        log.info("Feature added with id: {}", saved.getId());

        return tenantFeatureMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantFeatureResponseDto getFeatureById(Long id) {
        log.debug("Fetching tenant feature by id: {}", id);

        TenantFeature feature = tenantFeatureRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantFeatureNotFoundException.byId(id));

        return tenantFeatureMapper.toResponseDto(feature);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantFeatureResponseDto getFeatureByCode(Long tenantId, String featureCode) {
        log.debug("Fetching feature {} for tenant {}", featureCode, tenantId);

        TenantFeature feature = tenantFeatureRepository.findByTenantIdAndFeatureCode(tenantId, featureCode)
                .orElseThrow(() -> TenantFeatureNotFoundException.byCode(tenantId, featureCode));

        return tenantFeatureMapper.toResponseDto(feature);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getFeaturesByTenant(Long tenantId) {
        log.debug("Fetching all features for tenant {}", tenantId);

        List<TenantFeature> features = tenantFeatureRepository.findByTenantId(tenantId);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getActiveFeaturesByTenant(Long tenantId) {
        log.debug("Fetching active features for tenant {}", tenantId);

        List<TenantFeature> features = tenantFeatureRepository.findByTenantIdAndIsActive(tenantId, true);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getInactiveFeaturesByTenant(Long tenantId) {
        log.debug("Fetching inactive features for tenant {}", tenantId);

        List<TenantFeature> features = tenantFeatureRepository.findByTenantIdAndIsActive(tenantId, false);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getPlanFeatures(Long tenantId) {
        log.debug("Fetching plan features for tenant {}", tenantId);

        List<TenantFeature> features = tenantFeatureRepository.findByTenantIdAndIsPlanFeature(tenantId, true);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getAddOnFeatures(Long tenantId) {
        log.debug("Fetching add-on features for tenant {}", tenantId);

        List<TenantFeature> features = tenantFeatureRepository.findByTenantIdAndIsPlanFeatureFalse(tenantId);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantFeatureResponseDto> getTenantsWithFeature(String featureCode) {
        log.debug("Fetching tenants with feature {}", featureCode);

        List<TenantFeature> features = tenantFeatureRepository.findByFeatureCodeAndIsActive(featureCode, true);
        return tenantFeatureMapper.toResponseDtoList(features);
    }

    @Override
    public TenantFeatureResponseDto enableFeature(Long id) {
        log.info("Enabling feature id: {}", id);

        TenantFeature feature = tenantFeatureRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantFeatureNotFoundException.byId(id));

        feature.enable();
        TenantFeature saved = tenantFeatureRepository.save(feature);
        log.info("Feature enabled id: {}", saved.getId());

        return tenantFeatureMapper.toResponseDto(saved);
    }

    @Override
    public TenantFeatureResponseDto disableFeature(Long id) {
        log.info("Disabling feature id: {}", id);

        TenantFeature feature = tenantFeatureRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantFeatureNotFoundException.byId(id));

        feature.disable();
        TenantFeature saved = tenantFeatureRepository.save(feature);
        log.info("Feature disabled id: {}", saved.getId());

        return tenantFeatureMapper.toResponseDto(saved);
    }

    @Override
    public void deleteFeature(Long id) {
        log.info("Soft-deleting feature id: {}", id);

        TenantFeature feature = tenantFeatureRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantFeatureNotFoundException.byId(id));

        feature.softDelete(null);
        tenantFeatureRepository.save(feature);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tenantHasFeature(Long tenantId, String featureCode) {
        return tenantFeatureRepository.existsByTenantIdAndFeatureCode(tenantId, featureCode);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFeaturesByTenant(Long tenantId) {
        return tenantFeatureRepository.countByTenantId(tenantId);
    }
}
