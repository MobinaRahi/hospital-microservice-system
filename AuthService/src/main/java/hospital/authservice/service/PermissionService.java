package hospital.authservice.service;

import hospital.authservice.dto.permission.PermissionCreateDto;
import hospital.authservice.dto.permission.PermissionResponseDto;
import hospital.authservice.dto.permission.PermissionUpdateDto;
import hospital.authservice.model.Permission;
import lombok.NonNull;

import java.util.Set;

public interface PermissionService  {

    PermissionResponseDto createPermission(@NonNull PermissionCreateDto createDto);
    Set<PermissionResponseDto> createPermissionsBulk(@NonNull Set<PermissionCreateDto> createDtos);
    PermissionResponseDto getPermissionById(@NonNull Long id);
    PermissionResponseDto getPermissionByName(@NonNull String name);
    Set<PermissionResponseDto> getAllPermissions();
    Set<PermissionResponseDto> getPermissionsByNames(@NonNull Set<String> names);
    Set<PermissionResponseDto> getPermissionsByRoleId(@NonNull Long roleId);
    Set<PermissionResponseDto> getPermissionsByUserId(@NonNull Long userId);
    Permission getPermissionEntityById(@NonNull Long id);
    PermissionResponseDto updatePermission(@NonNull Long permissionId, @NonNull PermissionUpdateDto updateDto);
    Long countAllPermissions();
    void deletePermission(@NonNull Long permissionId);
    void hardDeletePermission(@NonNull Long permissionId);
    boolean existsByName(@NonNull String name);
}