package com.hospital.superadmin.service.impl;

import com.hospital.superadmin.dto.adminuser.AdminUserCreateDto;
import com.hospital.superadmin.dto.adminuser.AdminUserResponseDto;
import com.hospital.superadmin.dto.adminuser.AdminUserUpdateDto;
import com.hospital.superadmin.mapper.AdminUserMapper;
import com.hospital.superadmin.model.AdminUser;
import com.hospital.superadmin.model.enums.AdminStatus;
import com.hospital.superadmin.repository.AdminUserRepository;
import com.hospital.superadmin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link AdminUserService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;

    @Override
    public AdminUserResponseDto createAdmin(AdminUserCreateDto dto) {
        log.info("Creating admin user for userId: {}", dto.getUserId());

        if (adminUserRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Admin with email '" + dto.getEmail() + "' already exists");
        }

        if (adminUserRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("Admin with userId " + dto.getUserId() + " already exists");
        }

        AdminUser admin = adminUserMapper.toEntity(dto);
        AdminUser saved = adminUserRepository.save(admin);
        log.info("Admin created with id: {}", saved.getId());

        return adminUserMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDto getAdminById(Long id) {
        log.debug("Fetching admin by id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        return adminUserMapper.toResponseDto(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDto getAdminByUserId(Long userId) {
        log.debug("Fetching admin by userId: {}", userId);

        AdminUser admin = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin with userId " + userId + " not found"));

        return adminUserMapper.toResponseDto(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDto getAdminByEmail(String email) {
        log.debug("Fetching admin by email: {}", email);

        AdminUser admin = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin with email '" + email + "' not found"));

        return adminUserMapper.toResponseDto(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllAdmins() {
        log.debug("Fetching all admins");

        List<AdminUser> admins = adminUserRepository.findAllNotDeleted();
        return adminUserMapper.toResponseDtoList(admins);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAdminsByStatus(AdminStatus status) {
        log.debug("Fetching admins by status: {}", status);

        List<AdminUser> admins = adminUserRepository.findByStatus(status);
        return adminUserMapper.toResponseDtoList(admins);
    }

    @Override
    public AdminUserResponseDto updateAdmin(Long id, AdminUserUpdateDto dto) {
        log.info("Updating admin id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        adminUserMapper.updateEntity(dto, admin);
        AdminUser saved = adminUserRepository.save(admin);

        return adminUserMapper.toResponseDto(saved);
    }

    @Override
    public AdminUserResponseDto suspendAdmin(Long id) {
        log.info("Suspending admin id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        admin.suspend();
        AdminUser saved = adminUserRepository.save(admin);

        return adminUserMapper.toResponseDto(saved);
    }

    @Override
    public AdminUserResponseDto activateAdmin(Long id) {
        log.info("Activating admin id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        admin.activate();
        AdminUser saved = adminUserRepository.save(admin);

        return adminUserMapper.toResponseDto(saved);
    }

    @Override
    public AdminUserResponseDto deactivateAdmin(Long id) {
        log.info("Deactivating admin id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        admin.deactivate();
        AdminUser saved = adminUserRepository.save(admin);

        return adminUserMapper.toResponseDto(saved);
    }

    @Override
    public void deleteAdmin(Long id) {
        log.info("Soft-deleting admin id: {}", id);

        AdminUser admin = adminUserRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + id + " not found"));

        admin.softDelete(null);
        adminUserRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return adminUserRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIdExists(Long userId) {
        return adminUserRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AdminStatus status) {
        return adminUserRepository.countByStatus(status);
    }
}
