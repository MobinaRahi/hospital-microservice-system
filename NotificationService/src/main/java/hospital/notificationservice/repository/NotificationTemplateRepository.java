package hospital.notificationservice.repository;

import hospital.notificationservice.model.NotificationTemplate;
import hospital.notificationservice.model.enums.TemplateType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for NotificationTemplate entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByName - Template by name</li>
 *   <li>findByType - Templates by type (SMS/EMAIL)</li>
 *   <li>findByIsActive - Active/inactive templates</li>
 *   <li>findActiveByType - Active templates by type</li>
 *   <li>existsByName - Check name uniqueness</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface NotificationTemplateRepository extends BaseEntityRepository<NotificationTemplate, Long> {

    /**
     * Finds a template by its name.
     *
     * @param name the template name
     * @return template if found
     */
    Optional<NotificationTemplate> findByName(String name);

    /**
     * Finds templates by type (SMS or EMAIL).
     *
     * @param type the template type
     * @return list of templates with the type
     */
    List<NotificationTemplate> findByType(TemplateType type);

    /**
     * Finds templates by active status.
     *
     * @param isActive whether the template is active
     * @return list of active/inactive templates
     */
    List<NotificationTemplate> findByIsActive(Boolean isActive);

    /**
     * Finds active templates by type.
     * Used for selecting available templates for sending notifications.
     *
     * @param type     the template type
     * @param isActive whether the template is active
     * @return list of matching templates
     */
    List<NotificationTemplate> findByTypeAndIsActive(TemplateType type, Boolean isActive);

    /**
     * Checks if a template with the given name already exists.
     *
     * @param name the template name to check
     * @return true if a template with this name exists
     */
    boolean existsByName(String name);

    /**
     * Finds templates by name pattern (case-insensitive).
     *
     * @param name the name pattern
     * @return list of matching templates
     */
    List<NotificationTemplate> findByNameContainingIgnoreCase(String name);

    /**
     * Counts active templates.
     *
     * @param isActive whether the template is active
     * @return number of active/inactive templates
     */
    long countByIsActive(Boolean isActive);
}
