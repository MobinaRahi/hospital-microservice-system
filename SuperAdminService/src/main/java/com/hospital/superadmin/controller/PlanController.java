package com.hospital.superadmin.controller;

import com.hospital.superadmin.dto.plan.PlanCreateDto;
import com.hospital.superadmin.dto.plan.PlanResponseDto;
import com.hospital.superadmin.dto.plan.PlanUpdateDto;
import com.hospital.superadmin.model.enums.PlanType;
import com.hospital.superadmin.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST Controller for Plan management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/super-admin/plans — Create new plan</li>
 *   <li>GET    /api/v1/super-admin/plans — Get all / filter</li>
 *   <li>GET    /api/v1/super-admin/plans/{id} — Get by ID</li>
 *   <li>PUT    /api/v1/super-admin/plans/{id} — Update</li>
 *   <li>PUT    /api/v1/super-admin/plans/{id}/activate — Activate</li>
 *   <li>PUT    /api/v1/super-admin/plans/{id}/deactivate — Deactivate</li>
 *   <li>DELETE /api/v1/super-admin/plans/{id} — Soft delete</li>
 *   <li>GET    /api/v1/super-admin/plans/search — Search by name</li>
 *   <li>GET    /api/v1/super-admin/plans/active — Get active plans</li>
 *   <li>GET    /api/v1/super-admin/plans/count — Count statistics</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/super-admin/plans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Plans", description = "Subscription plan management")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(summary = "Create a new subscription plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created"),
            @ApiResponse(responseCode = "409", description = "Plan name already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<PlanResponseDto> createPlan(@Valid @RequestBody PlanCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(dto));
    }

    @GetMapping
    @Operation(summary = "Get all plans with optional filters")
    public ResponseEntity<List<PlanResponseDto>> getAllPlans(
            @RequestParam(required = false) PlanType planType,
            @RequestParam(required = false) Boolean active) {

        if (planType != null) {
            return ResponseEntity.ok(planService.getPlansByType(planType));
        }
        if (Boolean.TRUE.equals(active)) {
            return ResponseEntity.ok(planService.getActivePlans());
        }
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get plan by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan found"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponseDto> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get plan by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan found"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponseDto> getPlanByName(@PathVariable String name) {
        return ResponseEntity.ok(planService.getPlanByName(name));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan updated"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponseDto> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody PlanUpdateDto dto) {
        return ResponseEntity.ok(planService.updatePlan(id, dto));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan activated"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponseDto> activatePlan(@PathVariable Long id) {
        return ResponseEntity.ok(planService.activatePlan(id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan deactivated"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponseDto> deactivatePlan(@PathVariable Long id) {
        return ResponseEntity.ok(planService.deactivatePlan(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a plan")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search plans by name")
    public ResponseEntity<List<PlanResponseDto>> searchPlans(
            @RequestParam String name) {
        return ResponseEntity.ok(planService.searchPlansByName(name));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active plans")
    public ResponseEntity<List<PlanResponseDto>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }

    @GetMapping("/count")
    @Operation(summary = "Get plan count statistics")
    public ResponseEntity<Long> countPlans(
            @RequestParam(required = false) PlanType planType) {
        if (planType != null) {
            return ResponseEntity.ok(planService.countByPlanType(planType));
        }
        return ResponseEntity.ok(planService.countActivePlans());
    }

    @GetMapping("/check-name")
    @Operation(summary = "Check if a plan name is available")
    public ResponseEntity<Boolean> checkName(
            @RequestParam String name) {
        return ResponseEntity.ok(!planService.planNameExists(name));
    }
}
