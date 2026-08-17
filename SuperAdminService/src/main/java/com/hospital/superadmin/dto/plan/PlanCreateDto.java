package com.hospital.superadmin.dto.plan;

import com.hospital.superadmin.model.enums.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new subscription plan.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreateDto {

    @NotBlank(message = "Plan name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotNull(message = "Plan type is required")
    private PlanType planType;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Max users is required")
    private Integer maxUsers;

    @NotNull(message = "Max patients is required")
    private Integer maxPatients;

    @NotNull(message = "Max appointments per month is required")
    private Integer maxAppointmentsPerMonth;

    @NotNull(message = "Storage limit (MB) is required")
    private Integer storageLimitMB;

    @NotNull(message = "Monthly price is required")
    @PositiveOrZero(message = "Monthly price must be zero or positive")
    private BigDecimal monthlyPrice;

    @PositiveOrZero(message = "Annual price must be zero or positive")
    private BigDecimal annualPrice;

    @Size(max = 50, message = "Support level must be at most 50 characters")
    private String supportLevel;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Integer sortOrder = 0;
}
