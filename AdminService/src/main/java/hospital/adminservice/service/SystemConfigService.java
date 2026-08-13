package hospital.adminservice.service;

import hospital.adminservice.dto.systemconfig.SystemConfigCreateDto;
import hospital.adminservice.dto.systemconfig.SystemConfigResponseDto;
import hospital.adminservice.dto.systemconfig.SystemConfigUpdateDto;

import java.util.List;

/**
 * Service interface for SystemConfiguration management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each configuration key must be unique</li>
 *   <li>Configurations can be categorized (e.g., "BILLING", "APPOINTMENT")</li>
 *   <li>isEditable flag prevents accidental changes to critical settings</li>
 *   <li>Supports different data types: STRING, INTEGER, BOOLEAN</li>
 *   <li>Values are cached with TTL of 5 minutes</li>
 *   <li>Soft delete supported</li>
 * </ul>
 *
 * <p><strong>Caching Strategy:</strong></p>
 * <ul>
 *   <li>Read operations are cached with TTL of 5 minutes</li>
 *   <li>Write operations evict the cache</li>
 *   <li>Cache name: "systemConfig"</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface SystemConfigService {

    /**
     * Creates a new system configuration.
     *
     * @param dto the configuration creation data
     * @return the created configuration
     */
    SystemConfigResponseDto createConfig(SystemConfigCreateDto dto);

    /**
     * Gets a configuration by its ID.
     *
     * @param id the configuration ID
     * @return the configuration
     */
    SystemConfigResponseDto getConfigById(Long id);

    /**
     * Gets a configuration by its unique key.
     * Result is cached for 5 minutes.
     *
     * @param configKey the configuration key
     * @return the configuration
     */
    SystemConfigResponseDto getConfigByKey(String configKey);

    /**
     * Gets configurations by category.
     * Result is cached for 5 minutes.
     *
     * @param category the category
     * @return list of configurations in the category
     */
    List<SystemConfigResponseDto> getConfigsByCategory(String category);

    /**
     * Gets all editable configurations.
     *
     * @return list of editable configurations
     */
    List<SystemConfigResponseDto> getEditableConfigs();

    /**
     * Gets a configuration value as a string.
     *
     * @param configKey the configuration key
     * @return the configuration value or null if not found
     */
    String getConfigValue(String configKey);

    /**
     * Gets a configuration value with a default fallback.
     *
     * @param configKey    the configuration key
     * @param defaultValue default value if not found
     * @return the configuration value or default
     */
    String getConfigValueOrDefault(String configKey, String defaultValue);

    /**
     * Gets a configuration value as an integer.
     *
     * @param configKey the configuration key
     * @return the configuration value as integer or null
     */
    Integer getIntConfigValue(String configKey);

    /**
     * Gets a configuration value as a boolean.
     *
     * @param configKey the configuration key
     * @return the configuration value as boolean or null
     */
    Boolean getBoolConfigValue(String configKey);

    /**
     * Updates a configuration value.
     * Automatically evicts the cache.
     *
     * @param id          the configuration ID
     * @param dto         the update data
     * @return the updated configuration
     */
    SystemConfigResponseDto updateConfig(Long id, SystemConfigUpdateDto dto);

    /**
     * Updates a configuration value directly by key.
     *
     * @param configKey   the configuration key
     * @param configValue the new value
     */
    void updateConfigValue(String configKey, String configValue);

    /**
     * Soft-deletes a configuration.
     *
     * @param id the configuration ID
     */
    void deleteConfig(Long id);

    /**
     * Checks if a configuration key is already in use.
     *
     * @param configKey the key to check
     * @return true if the key exists
     */
    boolean keyExists(String configKey);
}
