package hospital.authservice.controller;

import hospital.authservice.dto.permission.PermissionCreateDto;
import hospital.authservice.dto.permission.PermissionResponseDto;
import hospital.authservice.dto.permission.PermissionUpdateDto;
import hospital.authservice.dto.response.ApiResponse;
import hospital.authservice.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Management", description = "Permission APIs")
public class PermissionApi {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponseDto>> create(@Valid @RequestBody PermissionCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(permissionService.createPermission(dto), "Permission created", HttpStatus.CREATED.value()));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> bulk(@Valid @RequestBody Set<PermissionCreateDto> dtoList) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(permissionService.createPermissionsBulk(dtoList), "Permissions created", HttpStatus.CREATED.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getPermissionById(id), "Permission found", HttpStatus.OK.value()));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getPermissionByName(name), "Permission found", HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> all() {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getAllPermissions(), "Permissions loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/by-names")
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> byNames(@RequestParam Set<String> names) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getPermissionsByNames(names), "Permissions loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> byRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getPermissionsByRoleId(roleId), "Role permissions loaded", HttpStatus.OK.value()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getPermissionsByUserId(userId), "User permissions loaded", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> update(@PathVariable Long id, @Valid @RequestBody PermissionUpdateDto dto) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.updatePermission(id, dto), "Permission updated", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.success("Permission deleted", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDelete(@PathVariable Long id) {
        permissionService.hardDeletePermission(id);
        return ResponseEntity.ok(ApiResponse.success("Permission hard deleted", HttpStatus.OK.value()));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.existsByName(name), "Permission existence checked", HttpStatus.OK.value()));
    }
}