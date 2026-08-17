package com.hospital.superadmin.dto.adminuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hospital.superadmin.model.enums.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning admin user data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserResponseDto {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private AdminStatus status;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
