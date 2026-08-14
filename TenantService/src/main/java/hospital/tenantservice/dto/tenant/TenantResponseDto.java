package hospital.tenantservice.dto.tenant;

import hospital.tenantservice.model.enums.IndustryType;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning tenant data in API responses.
 *
 * <p>Includes all tenant information, current plan details, and usage statistics.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponseDto {

    private Long id;

    // Basic Information
    private String name;
    private String subdomain;
    private String adminEmail;
    private String adminPhone;

    // Subscription
    private PlanType plan;
    private TenantStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    // Plan Limits
    private Integer maxUsers;
    private Integer maxPatients;
    private Integer maxAppointmentsPerMonth;
    private Integer storageLimitMB;
    private String supportLevel;

    // Current Usage
    private Integer currentUsers;
    private Integer currentPatients;
    private Integer currentMonthAppointments;

    // Contact & Location
    private String address;
    private String city;
    private String country;
    private String phone;
    private String website;
    private IndustryType industry;
    private String description;
    private String taxNumber;
    private String timezone;

    // Branding
    private String logoUrl;

    // Status
    private Boolean isActive;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed Fields
    private Boolean isExpired;
    private Boolean isOperational;
}
