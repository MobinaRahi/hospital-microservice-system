package hospital.authservice.service.impl;

import hospital.authservice.dto.role.RoleCreateDto;
import hospital.authservice.dto.role.RoleResponseDto;
import hospital.authservice.dto.role.RoleUpdateDto;

import hospital.authservice.exception.role.RoleNotFoundException;
import hospital.authservice.mapper.RoleMapper;
import hospital.authservice.model.Role;
import hospital.authservice.model.enums.RoleName;
import hospital.authservice.repository.RoleRepository;
import hospital.authservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponseDto createRole(RoleCreateDto createDto) {
        log.info("Creating new role with name: {}", createDto.getName());

        Role role = roleMapper.toEntity(createDto);
        Role saved = roleRepository.save(role);
        log.info("Role created with id: {}", saved.getId());
        return roleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public RoleResponseDto updateRole(Long roleId, RoleUpdateDto updateDto) {
        log.info("Updating role with id: {}", roleId);

        Role role = findNotDeletedRoleById(roleId);

        if (updateDto.getDescription() != null) {
            role.setDescription(updateDto.getDescription());
        }

        Role updated = roleRepository.save(role);
        log.info("Role updated with id: {}", updated.getId());
        return roleMapper.toResponseDto(updated);
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        log.debug("Fetching role by id: {}", id);
        Role role = findNotDeletedRoleById(id);
        return roleMapper.toResponseDto(role);
    }

    @Override
    public RoleResponseDto getRoleByNameWithPermissions(RoleName name) {
        log.debug("Fetching role by name with permissions: {}", name);
        return roleRepository.findNotDeletedByNameWithPermissions(name)
                .map(roleMapper::toResponseDto)
                .orElseThrow(() -> new RoleNotFoundException(name));
    }

    @Override
    public List<RoleResponseDto> getAllRoles() {
        log.debug("Fetching all non‑deleted roles");
        return roleRepository.findAll().stream()
                .filter(role -> !role.isDeleted())
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponseDto> getRolesByUserId(Long userId) {
        log.debug("Fetching roles for user id: {}", userId);
        Set<Role> roles = roleRepository.findAllNotDeletedRolesByUserId(userId);
        return roles.stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Role getRoleEntityById(Long id) {
        return findNotDeletedRoleById(id);
    }

    @Override
    @Transactional
    public RoleResponseDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        log.info("Assigning permissions {} to role id: {}", permissionIds, roleId);
        Role role = findNotDeletedRoleById(roleId);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public RoleResponseDto addPermissionToRole(Long roleId, Long permissionId) {
        log.info("Adding permission {} to role id: {}", permissionId, roleId);
        Role role = findNotDeletedRoleById(roleId);
        // Load permission and add to role's permissions set
        // role.getPermissions().add(permission);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public RoleResponseDto removePermissionFromRole(Long roleId, Long permissionId) {
        log.info("Removing permission {} from role id: {}", permissionId, roleId);
        Role role = findNotDeletedRoleById(roleId);
        // role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        Role saved = roleRepository.save(role);
        return roleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        log.info("Soft‑deleting role with id: {}", roleId);
        Role role = findNotDeletedRoleById(roleId);
        role.setDeleted(true); // assuming BaseEntity has deleted flag
        roleRepository.save(role);
        log.info("Role soft‑deleted with id: {}", roleId);
    }

    @Override
    public boolean roleExists(RoleName name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public long countUsersByRoleId(Long roleId) {
        return roleRepository.countNotDeletedUsersByRoleId(roleId);
    }

    @Override
    public List<RoleResponseDto> getRolesByPermission(String permissionName) {
        log.debug("Fetching roles by permission: {}", permissionName);
        Set<Role> roles = roleRepository.findAllNotDeletedRolesByPermission(permissionName);
        return roles.stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleResponseDto> getAllRolesPaged(Pageable pageable) {
        log.debug("Fetching paged roles");
        return roleRepository.findAll(pageable)
                .map(roleMapper::toResponseDto);
    }

    @Override
    public List<RoleResponseDto> getRolesByNames(Set<RoleName> names) {
        if (CollectionUtils.isEmpty(names)) {
            return List.of();
        }
        Set<Role> roles = roleRepository.findByNameIn(names);
        return roles.stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countAllRoles() {
        return roleRepository.count();
    }

    @Override
    public long countActiveRoles() {
        return roleRepository.findAll().stream()
                .filter(role -> !role.isDeleted())
                .count();
    }

    // ------------------- private helpers -------------------

    private Role findNotDeletedRoleById(Long id) {
        return roleRepository.findById(id)
                .filter(role -> !role.isDeleted())
                .orElseThrow(() -> new RoleNotFoundException(id));
    }
}
