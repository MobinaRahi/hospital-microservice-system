package hospital.notificationservice.dto.inappnotification;

import hospital.notificationservice.model.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new in-app notification.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code userId} - User ID from AuthService</li>
 *   <li>{@code title} - Notification title (max 200 characters)</li>
 *   <li>{@code message} - Notification message (max 1000 characters)</li>
 *   <li>{@code type} - Notification type for categorization</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code relatedId} - Related business entity ID (appointment, prescription, etc.)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InAppNotificationCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Notification title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @NotBlank(message = "Notification message is required")
    @Size(max = 1000, message = "Message must be at most 1000 characters")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    private Long relatedId;
}
