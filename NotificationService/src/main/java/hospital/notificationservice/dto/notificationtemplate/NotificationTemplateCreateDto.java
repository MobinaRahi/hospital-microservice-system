package hospital.notificationservice.dto.notificationtemplate;

import hospital.notificationservice.model.enums.TemplateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new notification template.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Template name for identification (max 200 characters)</li>
 *   <li>{@code type} - Template type (SMS or EMAIL)</li>
 *   <li>{@code content} - Template content with variable placeholders (max 5000 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code subject} - Email subject line (for EMAIL type only, max 500 characters)</li>
 *   <li>{@code variables} - Comma-separated list of variable names (max 1000 characters)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateCreateDto {

    @NotBlank(message = "Template name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @NotNull(message = "Template type is required")
    private TemplateType type;

    @Size(max = 500, message = "Subject must be at most 500 characters")
    private String subject;

    @NotBlank(message = "Template content is required")
    @Size(max = 5000, message = "Content must be at most 5000 characters")
    private String content;

    @Size(max = 1000, message = "Variables must be at most 1000 characters")
    private String variables;
}
