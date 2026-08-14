package hospital.tenantservice.controller;

import hospital.tenantservice.dto.tenant.*;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.TenantStatus;
import hospital.tenantservice.service.TenantService;
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
 * REST Controller for Tenant (hospital/clinic) management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/tenant — Create new tenant</li>
 *   <li>GET    /api/v1/tenant — Get all / filter by status/plan</li>
 *   <li>GET    /api/v1/tenant/{id} — Get by ID</li>
 *   <li>GET    /api/v1/tenant/subdomain/{subdomain} — Get by subdomain</li>
 *   <li>PUT    /api/v1/tenant/{id} — Update tenant</li>
 *   <li>PUT    /api/v1/tenant/{id}/activate — Activate tenant</li>
 *   <li>PUT    /api/v1/tenant/{id}/deactivate — Deactivate tenant</li>
 *   <li>PUT    /api/v1/tenant/{id}/suspend — Suspend tenant</li>
 *   <li>PUT    /api/v1/tenant/{id}/upgrade — Upgrade plan</li>
 *   <li>PUT    /api/v1/tenant/{id}/downgrade — Downgrade plan</li>
 *   <li>GET    /api/v1/tenant/{id}/usage — Get usage statistics</li>
 *   <li>DELETE /api/v1/tenant/{id} — Soft delete</li>
 *   <li>GET    /api/v1/tenant/search — Search by name</li>
 *   <li>GET    /api/v1/tenant/expiring — Get expiring tenants</li>
 *   <li>GET    /api/v1/tenant/check-subdomain — Check subdomain availability</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tenants", description = "Hospital/clinic tenant management")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @Operation(summary = "Create a new tenant (hospital/clinic)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tenant created"),
            @ApiResponse(responseCode = "409", description = "Subdomain or name already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<TenantResponseDto> createTenant(@Valid @RequestBody TenantCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.createTenant(dto));
    }

    @GetMapping
    @Operation(summary = "Get all tenants with optional filters")
    public ResponseEntity<List<TenantResponseDto>> getAllTenants(
            @Parameter(description = "Filter by status") @RequestParam(required = false) TenantStatus status,
            @Parameter(description = "Filter by plan") @RequestParam(required = false) PlanType plan) {

        if (status != null) {
            return ResponseEntity.ok(tenantService.getTenantsByStatus(status));
        }
        if (plan != null) {
            return ResponseEntity.ok(tenantService.getTenantsByPlan(plan));
        }
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant found"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @GetMapping("/subdomain/{subdomain}")
    @Operation(summary = "Get tenant by subdomain")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant found"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> getTenantBySubdomain(@PathVariable String subdomain) {
        return ResponseEntity.ok(tenantService.getTenantBySubdomain(subdomain));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant updated"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantUpdateDto dto) {
        return ResponseEntity.ok(tenantService.updateTenant(id, dto));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant activated"),
            @ApiResponse(responseCode = "400", description = "Cannot activate tenant"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> activateTenant(
            @PathVariable Long id,
            @RequestBody(required = false) TenantStatusUpdateDto dto) {
        return ResponseEntity.ok(tenantService.activateTenant(id, dto));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant deactivated"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> deactivateTenant(
            @PathVariable Long id,
            @RequestBody(required = false) TenantStatusUpdateDto dto) {
        return ResponseEntity.ok(tenantService.deactivateTenant(id, dto));
    }

    @PutMapping("/{id}/suspend")
    @Operation(summary = "Suspend a tenant temporarily")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant suspended"),
            @ApiResponse(responseCode = "400", description = "Cannot suspend tenant"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> suspendTenant(
            @PathVariable Long id,
            @RequestBody(required = false) TenantStatusUpdateDto dto) {
        return ResponseEntity.ok(tenantService.suspendTenant(id, dto));
    }

    @PutMapping("/{id}/upgrade")
    @Operation(summary = "Upgrade tenant subscription plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan upgraded"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> upgradeTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantPlanChangeDto dto) {
        return ResponseEntity.ok(tenantService.upgradeTenant(id, dto));
    }

    @PutMapping("/{id}/downgrade")
    @Operation(summary = "Downgrade tenant subscription plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan downgraded"),
            @ApiResponse(responseCode = "400", description = "Cannot downgrade (usage exceeds limits)"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponseDto> downgradeTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantPlanChangeDto dto) {
        return ResponseEntity.ok(tenantService.downgradeTenant(id, dto));
    }

    @GetMapping("/{id}/usage")
    @Operation(summary = "Get tenant usage statistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usage statistics"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantUsageResponseDto> getTenantUsage(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantUsage(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a tenant")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search tenants by name")
    public ResponseEntity<List<TenantResponseDto>> searchTenants(
            @Parameter(description = "Name search pattern") @RequestParam String name) {
        return ResponseEntity.ok(tenantService.searchTenantsByName(name));
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get tenants with subscriptions expiring within N days")
    public ResponseEntity<List<TenantResponseDto>> getExpiringTenants(
            @Parameter(description = "Number of days") @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(tenantService.getTenantsExpiringWithin(days));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active tenants")
    public ResponseEntity<List<TenantResponseDto>> getActiveTenants() {
        return ResponseEntity.ok(tenantService.getActiveTenants());
    }

    @GetMapping("/check-subdomain")
    @Operation(summary = "Check if a subdomain is available")
    public ResponseEntity<Boolean> checkSubdomain(
            @Parameter(description = "Subdomain to check") @RequestParam String subdomain) {
        return ResponseEntity.ok(!tenantService.subdomainExists(subdomain));
    }

    @GetMapping("/count/status")
    @Operation(summary = "Count tenants by status")
    public ResponseEntity<Long> countByStatus(
            @Parameter(description = "Tenant status") @RequestParam TenantStatus status) {
        return ResponseEntity.ok(tenantService.countTenantsByStatus(status));
    }

    @GetMapping("/count/plan")
    @Operation(summary = "Count tenants by plan type")
    public ResponseEntity<Long> countByPlan(
            @Parameter(description = "Plan type") @RequestParam PlanType plan) {
        return ResponseEntity.ok(tenantService.countTenantsByPlan(plan));
    }
}
