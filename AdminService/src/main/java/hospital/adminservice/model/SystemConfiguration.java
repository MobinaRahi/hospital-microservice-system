package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a system-wide configuration setting.
 * Stores key-value pairs for system parameters.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each configuration key must be unique</li>
 *   <li>Configurations can be categorized (e.g., "BILLING", "APPOINTMENT", "GENERAL")</li>
 *   <li>isEditable flag prevents accidental changes to critical settings</li>
 *   <li>Supports different data types: STRING, INTEGER, BOOLEAN</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li>Key: "MAX_APPOINTMENTS_PER_DAY", Value: "50", Category: "APPOINTMENT"</li>
 *   <li>Key: "DEFAULT_CURRENCY", Value: "USD", Category: "BILLING"</li>
 *   <li>Key: "ENABLE_AUTO_REMINDER", Value: "true", Category: "NOTIFICATION"</li>
 * </ul>
 *
 * <p><strong>Caching:</strong></p>
 * <ul>
 *   <li>System configurations are cached using Spring Cache + Caffeine</li>
 *   <li>Cache TTL is 5 minutes (300 seconds)</li>
 *   <li>Changes to configurations automatically evict cache</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "system_configurations",
        indexes = {
                @Index(name = "idx_sys_config_key", columnList = "config_key", unique = true),
                @Index(name = "idx_sys_config_category", columnList = "category")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SystemConfiguration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique configuration key.
     * Example: "MAX_APPOINTMENTS_PER_DAY", "DEFAULT_CURRENCY"
     */
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    /**
     * Configuration value stored as string.
     * Converted to appropriate type using getter methods.
     */
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    /**
     * Category for grouping related configurations.
     * Example: "BILLING", "APPOINTMENT", "GENERAL"
     */
    @Column(length = 100)
    private String category;

    /**
     * Data type of the configuration value.
     * Supported types: STRING, INTEGER, BOOLEAN
     */
    @Column(name = "data_type", nullable = false, length = 50)
    @Builder.Default
    private String dataType = "STRING";

    /**
     * Human-readable description of the configuration.
     */
    @Column(length = 500)
    private String description;

    /**
     * Whether this configuration can be edited through the UI.
     * Critical settings should have this set to false.
     */
    @Column(name = "is_editable", nullable = false)
    @Builder.Default
    private Boolean isEditable = true;

    /**
     * Gets the configuration value with a default fallback.
     *
     * @param defaultValue the value to return if configValue is null
     * @return configValue if not null, otherwise defaultValue
     */
    public String getValueOrDefault(String defaultValue) {
        return configValue != null ? configValue : defaultValue;
    }

    /**
     * Gets the configuration value as an Integer.
     *
     * @return configValue as Integer, or null if conversion fails
     */
    public Integer getIntValue() {
        if (configValue == null) return null;
        try {
            return Integer.parseInt(configValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Gets the configuration value as a Boolean.
     *
     * @return configValue as Boolean, or null if conversion fails
     */
    public Boolean getBoolValue() {
        if (configValue == null) return null;
        return Boolean.parseBoolean(configValue);
    }
}
