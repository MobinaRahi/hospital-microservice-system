package hospital.coreservice.model;

import hospital.coreservice.model.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "auditLogEntity")
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "action", nullable = false, length = 255)
    private String action;

    @Column(name = "old_value", columnDefinition = "CLOB")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "CLOB")
    private String newValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AuditStatus status;

    @Column(name = "error_message", columnDefinition = "CLOB")
    private String errorMessage;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public static AuditLog success(User user, String action, String newValue, Long duration) {
        return AuditLog.builder()
                .user(user)
                .userName(user != null ? user.getFullName() : "SYSTEM")
                .action(action)
                .newValue(newValue)
                .status(AuditStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .duration(duration)
                .build();
    }

    public static AuditLog failure(User user, String action, String errorMessage, Long duration) {
        return AuditLog.builder()
                .user(user)
                .userName(user != null ? user.getFullName() : "SYSTEM")
                .action(action)
                .errorMessage(errorMessage)
                .status(AuditStatus.FAILURE)
                .timestamp(LocalDateTime.now())
                .duration(duration)
                .build();
    }

    public static AuditLog warning(User user, String action, String message, Long duration) {
        return AuditLog.builder()
                .user(user)
                .userName(user != null ? user.getFullName() : "SYSTEM")
                .action(action)
                .errorMessage(message)
                .status(AuditStatus.WARNING)
                .timestamp(LocalDateTime.now())
                .duration(duration)
                .build();
    }
}
