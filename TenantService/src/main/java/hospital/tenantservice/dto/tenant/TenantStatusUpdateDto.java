package hospital.tenantservice.dto.tenant;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating tenant status (activate/suspend/deactivate).
 *
 * <p>Used for administrative actions that change tenant lifecycle status.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantStatusUpdateDto {

    /**
     * Reason for the status change.
     * Example: "Payment overdue", "Policy violation", "Requested by admin"
     */
    @Size(max = 1000, message = "Reason must be at most 1000 characters")
    private String reason;

    /**
     * Additional notes for internal tracking.
     */
    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
