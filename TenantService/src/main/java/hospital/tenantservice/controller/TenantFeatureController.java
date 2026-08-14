package hospital.tenantservice.controller;

import hospital.tenantservice.dto.tenantfeature.TenantFeatureCreateDto;
import hospital.tenantservice.dto.tenantfeature.TenantFeatureResponseDto;
import hospital.tenantservice.service.TenantFeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Tenant Feature management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/tenant/features — Add feature to tenant</li>
 *   <li>GET    /api/v1/tenant/features/{id} — Get feature by ID</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId} — Get all features for tenant</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/code/{code} — Get specific feature</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/active — Get active features</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/inactive — Get inactive features</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/plan — Get plan features</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/addon — Get add-on features</li>
 *   <li>GET    /api/v1/tenant/features/code/{code} — Get tenants with feature</li>
 *   <li>PUT    /api/v1/tenant/features/{id}/enable — Enable feature</li>
 *   <li>PUT    /api/v1/tenant/features/{id}/disable — Disable feature</li>
 *   <li>DELETE /api/v1/tenant/features/{id} — Soft delete feature</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/has/{code} — Check if tenant has feature</li>
 *   <li>GET    /api/v1/tenant/features/tenant/{tenantId}/count — Count features</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/tenant/features")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tenant Features", description = "Feature flags management for tenants")
public class TenantFeatureController {

    private final TenantFeatureService tenantFeatureService;

    @PostMapping
    @Operation(summary = "Add a feature to a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feature added"),
            @ApiResponse(responseCode = "409", description = "Feature already exists for tenant"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<TenantFeatureResponseDto> addFeature(@Valid @RequestBody TenantFeatureCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantFeatureService.addFeature(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feature by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feature found"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<TenantFeatureResponseDto> getFeatureById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantFeatureService.getFeatureById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get all features for a tenant")
    public ResponseEntity<List<TenantFeatureResponseDto>> getFeaturesByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.getFeaturesByTenant(tenantId));
    }

    @GetMapping("/tenant/{tenantId}/code/{code}")
    @Operation(summary = "Get specific feature for a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feature found"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<TenantFeatureResponseDto> getFeatureByCode(
            @PathVariable Long tenantId,
            @PathVariable String code) {
        return ResponseEntity.ok(tenantFeatureService.getFeatureByCode(tenantId, code));
    }

    @GetMapping("/tenant/{tenantId}/active")
    @Operation(summary = "Get active features for a tenant")
    public ResponseEntity<List<TenantFeatureResponseDto>> getActiveFeaturesByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.getActiveFeaturesByTenant(tenantId));
    }

    @GetMapping("/tenant/{tenantId}/inactive")
    @Operation(summary = "Get inactive features for a tenant")
    public ResponseEntity<List<TenantFeatureResponseDto>> getInactiveFeaturesByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.getInactiveFeaturesByTenant(tenantId));
    }

    @GetMapping("/tenant/{tenantId}/plan")
    @Operation(summary = "Get plan-included features for a tenant")
    public ResponseEntity<List<TenantFeatureResponseDto>> getPlanFeatures(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.getPlanFeatures(tenantId));
    }

    @GetMapping("/tenant/{tenantId}/addon")
    @Operation(summary = "Get add-on features for a tenant")
    public ResponseEntity<List<TenantFeatureResponseDto>> getAddOnFeatures(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.getAddOnFeatures(tenantId));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get all tenants that have a specific feature enabled")
    public ResponseEntity<List<TenantFeatureResponseDto>> getTenantsWithFeature(@PathVariable String code) {
        return ResponseEntity.ok(tenantFeatureService.getTenantsWithFeature(code));
    }

    @PutMapping("/{id}/enable")
    @Operation(summary = "Enable a feature")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feature enabled"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<TenantFeatureResponseDto> enableFeature(@PathVariable Long id) {
        return ResponseEntity.ok(tenantFeatureService.enableFeature(id));
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "Disable a feature")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feature disabled"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<TenantFeatureResponseDto> disableFeature(@PathVariable Long id) {
        return ResponseEntity.ok(tenantFeatureService.disableFeature(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a feature")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        tenantFeatureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tenant/{tenantId}/has/{code}")
    @Operation(summary = "Check if a tenant has a specific feature")
    public ResponseEntity<Boolean> tenantHasFeature(
            @PathVariable Long tenantId,
            @PathVariable String code) {
        return ResponseEntity.ok(tenantFeatureService.tenantHasFeature(tenantId, code));
    }

    @GetMapping("/tenant/{tenantId}/count")
    @Operation(summary = "Count features for a tenant")
    public ResponseEntity<Long> countFeaturesByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantFeatureService.countFeaturesByTenant(tenantId));
    }
}
