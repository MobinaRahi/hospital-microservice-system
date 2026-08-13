package hospital.adminservice.repository;

import hospital.adminservice.model.SystemConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SystemConfiguration entity.
 *
 * <p><strong>Caching Strategy:</strong></p>
 * <ul>
 *   <li>Read operations are cached with TTL of 5 minutes</li>
 *   <li>Write operations evict the cache</li>
 *   <li>Cache name: "systemConfig"</li>
 * </ul>
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByConfigKey - Find by unique key</li>
 *   <li>findByCategory - Find by category</li>
 *   <li>findEditable - Find editable configs</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface SystemConfigRepository extends BaseEntityRepository<SystemConfiguration, Long> {

    /**
     * Finds a configuration by its unique key.
     * Results are cached for 5 minutes.
     *
     * @param configKey the configuration key
     * @return configuration if found
     */
    @Cacheable(value = "systemConfig", key = "#configKey")
    Optional<SystemConfiguration> findByConfigKey(String configKey);

    /**
     * Finds configurations by category.
     *
     * @param category the category
     * @return list of configurations in the category
     */
    @Cacheable(value = "systemConfig", key = "'category:' + #category")
    List<SystemConfiguration> findByCategory(String category);

    /**
     * Finds all editable configurations.
     *
     * @return list of editable configurations
     */
    List<SystemConfiguration> findByIsEditableTrue();

    /**
     * Checks if a configuration key exists.
     *
     * @param configKey the key to check
     * @return true if exists
     */
    boolean existsByConfigKey(String configKey);

    /**
     * Updates a configuration value and evicts cache.
     *
     * @param configKey   the configuration key
     * @param configValue the new value
     */
    @Modifying
    @Query("UPDATE SystemConfiguration sc SET sc.configValue = :configValue WHERE sc.configKey = :configKey")
    @CacheEvict(value = "systemConfig", key = "#configKey")
    void updateConfigValue(@Param("configKey") String configKey, @Param("configValue") String configValue);

    /**
     * Gets a configuration value with default fallback.
     *
     * @param configKey    the configuration key
     * @return configuration value or default
     */
    @Query("SELECT sc.configValue FROM SystemConfiguration sc WHERE sc.configKey = :configKey")
    String findConfigValueByKey(@Param("configKey") String configKey);
}
