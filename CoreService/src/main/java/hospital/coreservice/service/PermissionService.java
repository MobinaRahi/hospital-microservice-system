package hospital.coreservice.service;

import hospital.coreservice.dto.permission.PermissionCreateDto;
import hospital.coreservice.dto.permission.PermissionResponseDto;
import hospital.coreservice.dto.permission.PermissionUpdateDto;
import hospital.coreservice.model.Permission;
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
    void deletePermission(@NonNull Long permissionId);
    void hardDeletePermission(@NonNull Long permissionId);
    boolean existsByName(@NonNull String name);
}