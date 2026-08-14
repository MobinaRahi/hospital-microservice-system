package hospital.tenantservice.controller;

import hospital.tenantservice.dto.subscriptionhistory.SubscriptionHistoryResponseDto;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import hospital.tenantservice.service.SubscriptionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Subscription History (audit trail).
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>GET /api/v1/tenant/history/tenant/{tenantId} — Full history for tenant</li>
 *   <li>GET /api/v1/tenant/history/tenant/{tenantId}/recent — Most recent change</li>
 *   <li>GET /api/v1/tenant/history/type/{changeType} — History by change type</li>
 *   <li>GET /api/v1/tenant/history/upgrades/{newPlan} — Upgrades to specific plan</li>
 *   <li>GET /api/v1/tenant/history/scheduled — Scheduled changes in date range</li>
 *   <li>GET /api/v1/tenant/history/pending — Pending scheduled changes</li>
 *   <li>GET /api/v1/tenant/history/user/{userId} — Changes by admin user</li>
 *   <li>GET /api/v1/tenant/history/count/type/{changeType} — Count by change type</li>
 *   <li>GET /api/v1/tenant/history/count/tenant/{tenantId} — Count by tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/tenant/history")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subscription History", description = "Audit trail for subscription changes")
public class SubscriptionHistoryController {

    private final SubscriptionHistoryService subscriptionHistoryService;

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get full subscription history for a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History found"),
            @ApiResponse(responseCode = "404", description = "No history found for tenant")
    })
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getHistoryByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(subscriptionHistoryService.getHistoryByTenant(tenantId));
    }

    @GetMapping("/tenant/{tenantId}/recent")
    @Operation(summary = "Get most recent subscription change for a tenant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Most recent change"),
            @ApiResponse(responseCode = "404", description = "No history found")
    })
    public ResponseEntity<SubscriptionHistoryResponseDto> getMostRecentChange(@PathVariable Long tenantId) {
        return ResponseEntity.ok(subscriptionHistoryService.getMostRecentChange(tenantId));
    }

    @GetMapping("/type/{changeType}")
    @Operation(summary = "Get subscription history by change type")
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getHistoryByChangeType(
            @PathVariable SubscriptionChangeType changeType) {
        return ResponseEntity.ok(subscriptionHistoryService.getHistoryByChangeType(changeType));
    }

    @GetMapping("/upgrades/{newPlan}")
    @Operation(summary = "Get all upgrades to a specific plan")
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getUpgradesToPlan(
            @PathVariable SubscriptionChangeType newPlan) {
        return ResponseEntity.ok(subscriptionHistoryService.getUpgradesToPlan(newPlan));
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Get scheduled changes within a date range")
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getScheduledChanges(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(subscriptionHistoryService.getScheduledChanges(startDate, endDate));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending scheduled changes (future effective date)")
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getPendingScheduledChanges() {
        return ResponseEntity.ok(subscriptionHistoryService.getPendingScheduledChanges());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all changes made by a specific admin user")
    public ResponseEntity<List<SubscriptionHistoryResponseDto>> getChangesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionHistoryService.getChangesByUser(userId));
    }

    @GetMapping("/count/type/{changeType}")
    @Operation(summary = "Count changes by type")
    public ResponseEntity<Long> countByChangeType(@PathVariable SubscriptionChangeType changeType) {
        return ResponseEntity.ok(subscriptionHistoryService.countByChangeType(changeType));
    }

    @GetMapping("/count/tenant/{tenantId}")
    @Operation(summary = "Count total changes for a tenant")
    public ResponseEntity<Long> countByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(subscriptionHistoryService.countByTenant(tenantId));
    }
}
