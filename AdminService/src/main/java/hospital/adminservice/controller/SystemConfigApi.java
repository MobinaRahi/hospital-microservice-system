package hospital.adminservice.controller;

import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.dto.systemconfig.SystemConfigCreateDto;
import hospital.adminservice.dto.systemconfig.SystemConfigResponseDto;
import hospital.adminservice.dto.systemconfig.SystemConfigUpdateDto;
import hospital.adminservice.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for SystemConfiguration management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>SuperAdmin: Full CRUD access</li>
 *   <li>Admin: Read access + update editable configs</li>
 * </ul>
 *
 * <p><strong>Caching:</strong></p>
 * <ul>
 *   <li>Read operations are cached with TTL of 5 minutes</li>
 *   <li>Write operations automatically evict the cache</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/system-config")
@RequiredArgsConstructor
@Tag(name = "System Configuration", description = "System configuration CRUD and value retrieval APIs")
public class SystemConfigApi {

    private final SystemConfigService systemConfigService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new system configuration")
    public ResponseEntity<ApiResponse<SystemConfigResponseDto>> createConfig(
            @Valid @RequestBody SystemConfigCreateDto createDto) {
        SystemConfigResponseDto created = systemConfigService.createConfig(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "System config created successfully", HttpStatus.CREATED.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ═════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get system config by ID")
    public ResponseEntity<ApiResponse<SystemConfigResponseDto>> getConfigById(@PathVariable Long id) {
        SystemConfigResponseDto config = systemConfigService.getConfigById(id);
        return ResponseEntity.ok(ApiResponse.success(config, "System config retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/key/{configKey}")
    @Operation(summary = "Get system config by key (cached)")
    public ResponseEntity<ApiResponse<SystemConfigResponseDto>> getConfigByKey(@PathVariable String configKey) {
        SystemConfigResponseDto config = systemConfigService.getConfigByKey(configKey);
        return ResponseEntity.ok(ApiResponse.success(config, "System config retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get system configs by category (cached)")
    public ResponseEntity<ApiResponse<List<SystemConfigResponseDto>>> getConfigsByCategory(@PathVariable String category) {
        List<SystemConfigResponseDto> configs = systemConfigService.getConfigsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(configs, "System configs retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/editable")
    @Operation(summary = "Get all editable system configs")
    public ResponseEntity<ApiResponse<List<SystemConfigResponseDto>>> getEditableConfigs() {
        List<SystemConfigResponseDto> configs = systemConfigService.getEditableConfigs();
        return ResponseEntity.ok(ApiResponse.success(configs, "Editable system configs retrieved successfully", HttpStatus.OK.value()));
    }

    // ═════════════════════════════════════════════════════════════════
    // Value Retrieval
    // ═════════════════════════════════════════════════════════════════

    @GetMapping("/value/{configKey}")
    @Operation(summary = "Get config value as string")
    public ResponseEntity<ApiResponse<String>> getConfigValue(@PathVariable String configKey) {
        String value = systemConfigService.getConfigValue(configKey);
        return ResponseEntity.ok(ApiResponse.success(value, "Config value retrieved", HttpStatus.OK.value()));
    }

    @GetMapping("/value/{configKey}/default/{defaultValue}")
    @Operation(summary = "Get config value with default fallback")
    public ResponseEntity<ApiResponse<String>> getConfigValueOrDefault(
            @PathVariable String configKey,
            @PathVariable String defaultValue) {
        String value = systemConfigService.getConfigValueOrDefault(configKey, defaultValue);
        return ResponseEntity.ok(ApiResponse.success(value, "Config value retrieved", HttpStatus.OK.value()));
    }

    @GetMapping("/int/{configKey}")
    @Operation(summary = "Get config value as integer")
    public ResponseEntity<ApiResponse<Integer>> getIntConfigValue(@PathVariable String configKey) {
        Integer value = systemConfigService.getIntConfigValue(configKey);
        return ResponseEntity.ok(ApiResponse.success(value, "Config value retrieved", HttpStatus.OK.value()));
    }

    @GetMapping("/bool/{configKey}")
    @Operation(summary = "Get config value as boolean")
    public ResponseEntity<ApiResponse<Boolean>> getBoolConfigValue(@PathVariable String configKey) {
        Boolean value = systemConfigService.getBoolConfigValue(configKey);
        return ResponseEntity.ok(ApiResponse.success(value, "Config value retrieved", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @PutMapping("/{id}")
    @Operation(summary = "Update a system config by ID")
    public ResponseEntity<ApiResponse<SystemConfigResponseDto>> updateConfig(
            @PathVariable Long id,
            @Valid @RequestBody SystemConfigUpdateDto updateDto) {
        SystemConfigResponseDto updated = systemConfigService.updateConfig(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "System config updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/key/{configKey}/value/{configValue}")
    @Operation(summary = "Update config value directly by key (cache evicted)")
    public ResponseEntity<ApiResponse<Void>> updateConfigValue(
            @PathVariable String configKey,
            @PathVariable String configValue) {
        systemConfigService.updateConfigValue(configKey, configValue);
        return ResponseEntity.ok(ApiResponse.success("Config value updated successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a system config")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(@PathVariable Long id) {
        systemConfigService.deleteConfig(id);
        return ResponseEntity.ok(ApiResponse.success("System config deleted successfully", HttpStatus.OK.value()));
    }
}
