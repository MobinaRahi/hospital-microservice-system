package com.hospital.superadmin.dto.plan;

import com.hospital.superadmin.model.enums.PlanType;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing subscription plan.
 * All fields are optional - only provided fields will be updated.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanUpdateDto {

    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    private PlanType planType;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private Integer maxUsers;
    private Integer maxPatients;
    private Integer maxAppointmentsPerMonth;
    private Integer storageLimitMB;

    @PositiveOrZero(message = "Monthly price must be zero or positive")
    private BigDecimal monthlyPrice;

    @PositiveOrZero(message = "Annual price must be zero or positive")
    private BigDecimal annualPrice;

    @Size(max = 50, message = "Support level must be at most 50 characters")
    private String supportLevel;

    private Boolean isActive;
    private Integer sortOrder;
}
