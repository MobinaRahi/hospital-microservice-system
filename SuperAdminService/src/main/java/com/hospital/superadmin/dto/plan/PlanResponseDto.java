package com.hospital.superadmin.dto.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hospital.superadmin.model.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning plan data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanResponseDto {

    private Long id;
    private String name;
    private PlanType planType;
    private String description;
    private Integer maxUsers;
    private Integer maxPatients;
    private Integer maxAppointmentsPerMonth;
    private Integer storageLimitMB;
    private BigDecimal monthlyPrice;
    private BigDecimal annualPrice;
    private String supportLevel;
    private Boolean isActive;
    private Integer sortOrder;
    private Boolean isFree;
    private Boolean isEnterprise;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
