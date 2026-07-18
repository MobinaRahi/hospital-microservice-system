package hospital.coreservice.dto.audit_log;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * DTO for audit log response data.
 *
 * @author Mobina
 */
public class AuditLogResponseDto {
    private Long id;

    private Long userId;

    private String userName;

    private String action;

    private String oldValue;

    private String newValue;

    private AuditStatus status;

    private String errorMessage;

    private Long duration;

    private LocalDateTime timestamp;
}