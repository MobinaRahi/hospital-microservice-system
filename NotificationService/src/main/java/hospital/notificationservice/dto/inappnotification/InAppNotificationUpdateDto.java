package hospital.notificationservice.dto.inappnotification;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing in-app notification.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code userId}, {@code type}, and {@code relatedId}
 * are not updatable as they define the notification identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InAppNotificationUpdateDto {

    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @Size(max = 1000, message = "Message must be at most 1000 characters")
    private String message;

    private Boolean isRead;
}
