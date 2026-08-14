package hospital.notificationservice.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for RabbitMQ Messages.
 *
 * <p>Implements Serializable for RabbitMQ message serialization.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String recipient;
    private String subject;
    private String body;
    private Long templateId;
    private String type; // SMS, EMAIL, IN_APP
    private LocalDateTime createdAt;

}
