package hospital.tenantservice.repository;

import hospital.tenantservice.model.TenantFeature;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TenantFeature entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByTenantId - Find all features for a tenant</li>
 *   <li>findByTenantIdAndFeatureCode - Find specific feature for a tenant</li>
 *   <li>findByTenantIdAndIsActive - Find active/inactive features for a tenant</li>
 *   <li>findByFeatureCode - Find all tenants with a specific feature</li>
 *   <li>countByTenantId - Count features for a tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface TenantFeatureRepository extends BaseEntityRepository<TenantFeature, Long> {

    /**
     * Finds all features for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return list of features for the tenant
     */
    List<TenantFeature> findByTenantId(Long tenantId);

    /**
     * Finds a specific feature for a tenant.
     *
     * @param tenantId    the tenant ID
     * @param featureCode the feature code
     * @return Optional containing the feature if found
     */
    Optional<TenantFeature> findByTenantIdAndFeatureCode(Long tenantId, String featureCode);

    /**
     * Finds active features for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active features
     */
    List<TenantFeature> findByTenantIdAndIsActive(Long tenantId, Boolean isActive);

    /**
     * Finds all tenants that have a specific feature enabled.
     *
     * @param featureCode the feature code
     * @return list of tenant features with the specified feature
     */
    List<TenantFeature> findByFeatureCodeAndIsActive(String featureCode, Boolean isActive);

    /**
     * Finds plan-included features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of plan features
     */
    List<TenantFeature> findByTenantIdAndIsPlanFeature(Long tenantId, Boolean isPlanFeature);

    /**
     * Finds add-on features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of add-on features
     */
    List<TenantFeature> findByTenantIdAndIsPlanFeatureFalse(Long tenantId);

    /**
     * Counts features for a tenant.
     *
     * @param tenantId the tenant ID
     * @return number of features
     */
    long countByTenantId(Long tenantId);

    /**
     * Checks if a tenant has a specific feature.
     *
     * @param tenantId    the tenant ID
     * @param featureCode the feature code
     * @return true if the tenant has the feature
     */
    boolean existsByTenantIdAndFeatureCode(Long tenantId, String featureCode);

    /**
     * Deletes all features for a tenant.
     * Used when tenant is being removed.
     *
     * @param tenantId the tenant ID
     */
    void deleteByTenantId(Long tenantId);
}
