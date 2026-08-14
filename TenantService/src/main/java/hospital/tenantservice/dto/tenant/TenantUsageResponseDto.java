package hospital.tenantservice.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning tenant usage statistics.
 *
 * <p>Used for monitoring, billing, and plan limit enforcement.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUsageResponseDto {

    private Long tenantId;
    private String tenantName;

    // User Usage
    private Integer currentUsers;
    private Integer maxUsers;
    private Double userUsagePercent;

    // Patient Usage
    private Integer currentPatients;
    private Integer maxPatients;
    private Double patientUsagePercent;

    // Appointment Usage
    private Integer currentMonthAppointments;
    private Integer maxAppointmentsPerMonth;
    private Double appointmentUsagePercent;

    // Storage Usage
    private Integer storageUsedMB;
    private Integer storageLimitMB;
    private Double storageUsagePercent;

    // Status
    private Boolean isExpired;
    private LocalDateTime checkedAt;
}
