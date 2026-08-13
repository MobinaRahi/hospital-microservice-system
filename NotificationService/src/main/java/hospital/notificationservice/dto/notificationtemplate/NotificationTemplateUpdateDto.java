package hospital.notificationservice.dto.notificationtemplate;

import hospital.notificationservice.model.enums.TemplateType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing notification template.
 * All fields are optional - only provided fields will be updated.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateUpdateDto {

    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    private TemplateType type;

    @Size(max = 500, message = "Subject must be at most 500 characters")
    private String subject;

    @Size(max = 5000, message = "Content must be at most 5000 characters")
    private String content;

    @Size(max = 1000, message = "Variables must be at most 1000 characters")
    private String variables;

    private Boolean isActive;
}
