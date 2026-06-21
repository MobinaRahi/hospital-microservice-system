package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.role.RoleCreateDto;
import hospital.coreservice.dto.role.RoleResponseDto;
import hospital.coreservice.dto.role.RoleUpdateDto;
import hospital.coreservice.model.enums.RoleName;
import hospital.coreservice.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "Role and role-permission APIs")
public class RoleApi {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDto>> create(@Valid @RequestBody RoleCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(roleService.createRole(dto), "Role created", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> update(@PathVariable Long id, @Valid @RequestBody RoleUpdateDto dto) {
        return ResponseEntity.ok(ApiResponse.success(roleService.updateRole(id, dto), "Role updated", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRoleById(id), "Role found", HttpStatus.OK.value()));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getByName(@RequestParam RoleName name) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRoleByNameWithPermissions(name), "Role found", HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> all() {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllRoles(), "Roles loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<RoleResponseDto>>> paged(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllRolesPaged(pageable), "Roles loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRolesByUserId(userId), "User roles loaded", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponseDto>> assignPermissions(@PathVariable Long id, @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(ApiResponse.success(roleService.assignPermissionsToRole(id, permissionIds), "Permissions assigned", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/permissions/{permissionId}/add")
    public ResponseEntity<ApiResponse<RoleResponseDto>> addPermission(@PathVariable Long id, @PathVariable Long permissionId) {
        return ResponseEntity.ok(ApiResponse.success(roleService.addPermissionToRole(id, permissionId), "Permission added", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/permissions/{permissionId}/remove")
    public ResponseEntity<ApiResponse<RoleResponseDto>> removePermission(@PathVariable Long id, @PathVariable Long permissionId) {
        return ResponseEntity.ok(ApiResponse.success(roleService.removePermissionFromRole(id, permissionId), "Permission removed", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted", HttpStatus.OK.value()));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(roleService.roleExists(name), "Role existence checked", HttpStatus.OK.value()));
    }

    @GetMapping("/by-permission")
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> byPermission(@RequestParam String permissionName) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRolesByPermission(permissionName), "Roles loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/by-names")
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> byNames(@RequestParam Set<String> names) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRolesByNames(names), "Roles loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countAll() {
        return ResponseEntity.ok(ApiResponse.success(roleService.countAllRoles(), "Roles count", HttpStatus.OK.value()));
    }

    @GetMapping("/count/active")
    public ResponseEntity<ApiResponse<Long>> countActive() {
        return ResponseEntity.ok(ApiResponse.success(roleService.countActiveRoles(), "Active roles count", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/count-users")
    public ResponseEntity<ApiResponse<Long>> countUsers(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.countUsersByRoleId(id), "Users by role count", HttpStatus.OK.value()));
    }
}