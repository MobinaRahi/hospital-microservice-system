package hospital.coreservice.service.imp;

import hospital.coreservice.dto.permission.PermissionCreateDto;
import hospital.coreservice.dto.permission.PermissionResponseDto;
import hospital.coreservice.dto.permission.PermissionUpdateDto;

import hospital.coreservice.exception.permition.DuplicatePermissionException;
import hospital.coreservice.exception.permition.PermissionNotFoundException;
import hospital.coreservice.mapper.PermissionMapper;
import hospital.coreservice.model.Permission;
import hospital.coreservice.repository.PermissionRepository;
import hospital.coreservice.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImp implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    // ============================================================
    //  CREATE
    // ============================================================

    @Override
    @Transactional
    public PermissionResponseDto createPermission(@NonNull PermissionCreateDto createDtoList) {
        log.info("Creating new permission: {}", createDtoList.getName());

        if (permissionRepository.existsByName(createDtoList.getName())) {
            throw new DuplicatePermissionException(createDtoList.getName());
        }

        Permission permission = permissionMapper.toEntity(createDtoList);
        Permission saved = permissionRepository.save(permission);

        log.info("Permission created with id: {}", saved.getId());
        return permissionMapper.toResponseDto(saved);
    }

    @Override
    public Set<PermissionResponseDto> createPermissionsBulk(@NonNull Set<PermissionCreateDto> createDtoList) {
        log.info("Creating {} permissions in bulk", createDtoList.size());

        Set<Permission> permissions = new HashSet<>();

        for (PermissionCreateDto dto : createDtoList) {
            if (permissionRepository.existsByName(dto.getName())) {
                throw new DuplicatePermissionException(dto.getName());
            }
            permissions.add(permissionMapper.toEntity(dto));
        }

        Set<Permission> saved = new HashSet<>(permissionRepository.saveAll(permissions));
        log.info("{} permissions created successfully", saved.size());

        return saved.stream()
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    // ============================================================
    //  READ
    // ============================================================

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PermissionResponseDto getPermissionById(@NonNull Long id) {
        log.debug("Fetching permission by id: {}", id);
        Permission permission = findEntityById(id);
        return permissionMapper.toResponseDto(permission);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PermissionResponseDto getPermissionByName(@NonNull String name) {
        log.debug("Fetching permission by name: {}", name);
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new PermissionNotFoundException(name));
        return permissionMapper.toResponseDto(permission);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Set<PermissionResponseDto> getAllPermissions() {
        log.debug("Fetching all permissions");
        return permissionRepository.findAll().stream()
                .filter(p -> !p.isDeleted())
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Set<PermissionResponseDto> getPermissionsByNames(@NonNull Set<String> names) {
        log.debug("Fetching permissions by names: {}", names);
        return permissionRepository.findByNameIn(names).stream()
                .filter(p -> !p.isDeleted())
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Set<PermissionResponseDto> getPermissionsByRoleId(@NonNull Long roleId) {
        log.debug("Fetching permissions for role id: {}", roleId);
        return permissionRepository.findAllActivePermissionsByRoleId(roleId).stream()
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Set<PermissionResponseDto> getPermissionsByUserId(@NonNull Long userId) {
        log.debug("Fetching permissions for user id: {}", userId);
        return permissionRepository.findAllActivePermissionsByUserId(userId).stream()
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Permission getPermissionEntityById(@NonNull Long id) {
        log.debug("Fetching permission entity by id: {}", id);
        return findEntityById(id);
    }

    // ============================================================
    //  UPDATE
    // ============================================================

    @Override
    public PermissionResponseDto updatePermission(@NonNull Long permissionId, @NonNull PermissionUpdateDto updateDto) {
        log.info("Updating permission with id: {}", permissionId);

        Permission permission = findEntityById(permissionId);
        if (updateDto.getName() != null && !updateDto.getName().equals(permission.getName())) {
            if (permissionRepository.existsByName(updateDto.getName())) {
                throw new DuplicatePermissionException(updateDto.getName());
            }
        }

        permissionMapper.updateEntity(permission, updateDto);
        Permission updated = permissionRepository.save(permission);

        log.info("Permission updated: {}", updated.getName());
        return permissionMapper.toResponseDto(updated);
    }

    // ============================================================
    //  DELETE
    // ============================================================

    @Override
    public void deletePermission(@NonNull Long permissionId) {
        log.info("Soft deleting permission with id: {}", permissionId);

        Permission permission = findEntityById(permissionId);
        if (permission.getRoles() != null && !permission.getRoles().isEmpty()) {
            log.warn("Permission {} is assigned to {} roles. Soft deleting anyway.",
                    permission.getName(), permission.getRoles().size());
        }

        permission.setDeleted(true);
        permissionRepository.save(permission);

        log.info("Permission soft deleted: {}", permission.getName());
    }

    @Override
    public void hardDeletePermission(@NonNull Long permissionId) {
        log.warn("Hard deleting permission with id: {}", permissionId);

        Permission permission = findEntityById(permissionId);
        permissionRepository.delete(permission);

        log.warn("Permission hard deleted: {}", permission.getName());
    }

    // ============================================================
    //  UTILITY
    // ============================================================

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsByName(@NonNull String name) {
        return permissionRepository.existsByName(name);
    }

    // ============================================================
    //  PRIVATE HELPERS
    // ============================================================

    private Permission findEntityById(Long id) {
        return permissionRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new PermissionNotFoundException(id));
    }
}