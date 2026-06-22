package hospital.coreservice.service;

import hospital.coreservice.dto.role.RoleCreateDto;
import hospital.coreservice.dto.role.RoleResponseDto;
import hospital.coreservice.dto.role.RoleUpdateDto;
import hospital.coreservice.model.Role;
import hospital.coreservice.model.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleResponseDto createRole(RoleCreateDto createDto);

    RoleResponseDto updateRole(Long roleId, RoleUpdateDto updateDto);

    RoleResponseDto getRoleById(Long id);

    RoleResponseDto getRoleByNameWithPermissions(RoleName name);

    List<RoleResponseDto> getAllRoles();

    List<RoleResponseDto> getRolesByUserId(Long userId);

    Role getRoleEntityById(Long id);

    RoleResponseDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds);

    RoleResponseDto addPermissionToRole(Long roleId, Long permissionId);

    RoleResponseDto removePermissionFromRole(Long roleId, Long permissionId);

    void deleteRole(Long roleId);

    boolean roleExists(RoleName name);

    long countUsersByRoleId(Long roleId);

    List<RoleResponseDto> getRolesByPermission(String permissionName);

    Page<RoleResponseDto> getAllRolesPaged(Pageable pageable);

    List<RoleResponseDto> getRolesByNames(Set<RoleName> names);

    long countAllRoles();

    long countActiveRoles();
}