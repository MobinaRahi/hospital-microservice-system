package com.hospital.superadmin.dto.adminuser;

import com.hospital.superadmin.model.enums.AdminStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new super admin user.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreateDto {

    @NotNull(message = "User ID from AuthService is required")
    private Long userId;

    @NotBlank(message = "Full name is required")
    @Size(max = 200, message = "Full name must be at most 200 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String email;

    @Size(max = 100, message = "Role must be at most 100 characters")
    private String role;

    @Builder.Default
    private AdminStatus status = AdminStatus.ACTIVE;
}
