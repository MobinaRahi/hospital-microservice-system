package hospital.notificationservice.dto.emaillog;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new email log entry.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code to} - Recipient email address (valid email format)</li>
 *   <li>{@code subject} - Email subject (max 500 characters)</li>
 *   <li>{@code body} - Email body content (max 10000 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code templateId} - NotificationTemplate ID for reusable content</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLogCreateDto {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email address must be at most 255 characters")
    private String to;

    @NotBlank(message = "Email subject is required")
    @Size(max = 500, message = "Subject must be at most 500 characters")
    private String subject;

    @NotBlank(message = "Email body is required")
    @Size(max = 10000, message = "Body must be at most 10000 characters")
    private String body;

    private Long templateId;
}
