package com.hospital.superadmin.service;

import com.hospital.superadmin.dto.adminuser.AdminUserCreateDto;
import com.hospital.superadmin.dto.adminuser.AdminUserResponseDto;
import com.hospital.superadmin.dto.adminuser.AdminUserUpdateDto;
import com.hospital.superadmin.model.enums.AdminStatus;

import java.util.List;

/**
 * Service interface for AdminUser management.
 *
 * @author MobinaRahi
 */
public interface AdminUserService {

    AdminUserResponseDto createAdmin(AdminUserCreateDto dto);

    AdminUserResponseDto getAdminById(Long id);

    AdminUserResponseDto getAdminByUserId(Long userId);

    AdminUserResponseDto getAdminByEmail(String email);

    List<AdminUserResponseDto> getAllAdmins();

    List<AdminUserResponseDto> getAdminsByStatus(AdminStatus status);

    AdminUserResponseDto updateAdmin(Long id, AdminUserUpdateDto dto);

    AdminUserResponseDto suspendAdmin(Long id);

    AdminUserResponseDto activateAdmin(Long id);

    AdminUserResponseDto deactivateAdmin(Long id);

    void deleteAdmin(Long id);

    boolean emailExists(String email);

    boolean userIdExists(Long userId);

    long countByStatus(AdminStatus status);
}
