package hospital.tenantservice.service;

import hospital.tenantservice.dto.tenantfeature.TenantFeatureCreateDto;
import hospital.tenantservice.dto.tenantfeature.TenantFeatureResponseDto;

import java.util.List;

/**
 * Service interface for Tenant Feature management.
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
public interface TenantFeatureService {

    /**
     * Adds a new feature to a tenant.
     *
     * @param dto the feature creation data
     * @return the created feature
     */
    TenantFeatureResponseDto addFeature(TenantFeatureCreateDto dto);

    /**
     * Gets a feature by its ID.
     *
     * @param id the feature ID
     * @return the feature
     */
    TenantFeatureResponseDto getFeatureById(Long id);

    /**
     * Gets a specific feature for a tenant by feature code.
     *
     * @param tenantId    the tenant ID
     * @param featureCode the feature code
     * @return the feature
     */
    TenantFeatureResponseDto getFeatureByCode(Long tenantId, String featureCode);

    /**
     * Gets all features for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return list of features for the tenant
     */
    List<TenantFeatureResponseDto> getFeaturesByTenant(Long tenantId);

    /**
     * Gets active features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active features
     */
    List<TenantFeatureResponseDto> getActiveFeaturesByTenant(Long tenantId);

    /**
     * Gets inactive features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of inactive features
     */
    List<TenantFeatureResponseDto> getInactiveFeaturesByTenant(Long tenantId);

    /**
     * Gets plan-included features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of plan features
     */
    List<TenantFeatureResponseDto> getPlanFeatures(Long tenantId);

    /**
     * Gets add-on features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of add-on features
     */
    List<TenantFeatureResponseDto> getAddOnFeatures(Long tenantId);

    /**
     * Gets all tenants that have a specific feature enabled.
     *
     * @param featureCode the feature code
     * @return list of features across tenants
     */
    List<TenantFeatureResponseDto> getTenantsWithFeature(String featureCode);

    /**
     * Enables a feature.
     *
     * @param id the feature ID
     * @return the enabled feature
     */
    TenantFeatureResponseDto enableFeature(Long id);

    /**
     * Disables a feature.
     *
     * @param id the feature ID
     * @return the disabled feature
     */
    TenantFeatureResponseDto disableFeature(Long id);

    /**
     * Soft-deletes a feature.
     *
     * @param id the feature ID
     */
    void deleteFeature(Long id);

    /**
     * Checks if a tenant has a specific feature.
     *
     * @param tenantId    the tenant ID
     * @param featureCode the feature code
     * @return true if the tenant has the feature
     */
    boolean tenantHasFeature(Long tenantId, String featureCode);

    /**
     * Counts features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return number of features
     */
    long countFeaturesByTenant(Long tenantId);
}
