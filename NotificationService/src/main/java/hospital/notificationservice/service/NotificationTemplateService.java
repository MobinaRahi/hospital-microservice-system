package hospital.notificationservice.service;

import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateCreateDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateResponseDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateUpdateDto;
import hospital.notificationservice.model.enums.TemplateType;

import java.util.List;

/**
 * Service interface for Notification Template management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Templates have a name for easy identification</li>
 *   <li>type determines whether it's for SMS or Email</li>
 *   <li>subject is used for email templates only</li>
 *   <li>content can contain variables for dynamic replacement</li>
 *   <li>variables define the available placeholders in the content</li>
 *   <li>isActive flag controls whether the template can be used</li>
 *   <li>Inactive templates cannot be used for new notifications</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface NotificationTemplateService {

    /**
     * Creates a new notification template.
     * Initial status is active.
     *
     * @param dto the template creation data
     * @return the created template
     */
    NotificationTemplateResponseDto createTemplate(NotificationTemplateCreateDto dto);

    /**
     * Gets a template by its ID.
     *
     * @param id the template ID
     * @return the template
     */
    NotificationTemplateResponseDto getTemplateById(Long id);

    /**
     * Gets a template by its name.
     *
     * @param name the template name
     * @return the template
     */
    NotificationTemplateResponseDto getTemplateByName(String name);

    /**
     * Gets all templates.
     *
     * @return list of all templates
     */
    List<NotificationTemplateResponseDto> getAllTemplates();

    /**
     * Gets templates by type (SMS or Email).
     *
     * @param type the template type
     * @return list of templates with the type
     */
    List<NotificationTemplateResponseDto> getTemplatesByType(TemplateType type);

    /**
     * Gets active templates.
     *
     * @return list of active templates
     */
    List<NotificationTemplateResponseDto> getActiveTemplates();

    /**
     * Gets active templates by type.
     *
     * @param type the template type
     * @return list of active templates with the type
     */
    List<NotificationTemplateResponseDto> getActiveTemplatesByType(TemplateType type);

    /**
     * Searches templates by name pattern.
     *
     * @param name the name pattern
     * @return list of matching templates
     */
    List<NotificationTemplateResponseDto> searchTemplatesByName(String name);

    /**
     * Updates an existing template.
     *
     * @param id  the template ID
     * @param dto the update data
     * @return the updated template
     */
    NotificationTemplateResponseDto updateTemplate(Long id, NotificationTemplateUpdateDto dto);

    /**
     * Activates a template.
     * Sets isActive=true.
     *
     * @param id the template ID
     * @return the activated template
     */
    NotificationTemplateResponseDto activateTemplate(Long id);

    /**
     * Deactivates a template.
     * Sets isActive=false.
     * Inactive templates cannot be used for new notifications.
     *
     * @param id the template ID
     * @return the deactivated template
     */
    NotificationTemplateResponseDto deactivateTemplate(Long id);

    /**
     * Soft-deletes a template.
     *
     * @param id the template ID
     */
    void deleteTemplate(Long id);

    /**
     * Checks if a template name already exists.
     *
     * @param name the template name to check
     * @return true if exists
     */
    boolean templateNameExists(String name);
}
