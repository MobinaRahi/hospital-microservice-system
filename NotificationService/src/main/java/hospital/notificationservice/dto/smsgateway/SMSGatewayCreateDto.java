package hospital.notificationservice.dto.smsgateway;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new SMS gateway entry.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code to} - Recipient phone number (max 20 characters)</li>
 *   <li>{@code message} - SMS message content (max 1600 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code templateId} - NotificationTemplate ID for reusable content</li>
 *   <li>{@code provider} - SMS gateway provider name</li>
 *   <li>{@code cost} - Cost of sending this SMS in local currency</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSGatewayCreateDto {

    @NotBlank(message = "Recipient phone number is required")
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String to;

    @NotBlank(message = "SMS message is required")
    @Size(max = 1600, message = "Message must be at most 1600 characters")
    private String message;

    private Long templateId;

    @Size(max = 100, message = "Provider name must be at most 100 characters")
    private String provider;

    @Positive(message = "Cost must be positive")
    private Integer cost;
}
