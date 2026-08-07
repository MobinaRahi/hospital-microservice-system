package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderCreateDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderResponseDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import hospital.inventoryservice.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for PurchaseOrder management.
 * Provides full lifecycle management from creation to receipt.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → APPROVED → SENT → PARTIAL → COMPLETED
 *                                  ↑
 *                             (cancelled at any point)
 * </pre>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Order Management", description = "Purchase order CRUD and lifecycle APIs")
public class PurchaseOrderApi {

    // ==================== Dependencies ====================

    private final PurchaseOrderService purchaseOrderService;

    // ==================== Create ====================

    /**
     * Creates a new purchase order with items.
     *
     * <p><strong>Required fields:</strong> supplierId, orderDate, items</p>
     * <p><strong>Optional fields:</strong> expectedDeliveryDate, notes</p>
     */
    @PostMapping
    @Operation(summary = "Create a new purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderCreateDto createDto) {
        PurchaseOrderResponseDto created = purchaseOrderService.createPurchaseOrder(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Purchase order created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing purchase order.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a purchase order by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> updatePurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderUpdateDto updateDto) {
        PurchaseOrderResponseDto updated = purchaseOrderService.updatePurchaseOrder(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Purchase order updated successfully", HttpStatus.OK.value()));
    }

    // ==================== Status Transitions ====================

    /**
     * Approves a purchase order.
     * Status transition: PENDING → APPROVED
     */
    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve a purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> approveOrder(@PathVariable Long id) {
        PurchaseOrderResponseDto approved = purchaseOrderService.approveOrder(id);
        return ResponseEntity.ok(ApiResponse.success(approved, "Purchase order approved successfully", HttpStatus.OK.value()));
    }

    /**
     * Marks a purchase order as sent to the supplier.
     * Status transition: APPROVED → SENT
     */
    @PatchMapping("/{id}/send")
    @Operation(summary = "Mark purchase order as sent")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> sendOrder(@PathVariable Long id) {
        PurchaseOrderResponseDto sent = purchaseOrderService.sendOrder(id);
        return ResponseEntity.ok(ApiResponse.success(sent, "Purchase order sent successfully", HttpStatus.OK.value()));
    }

    /**
     * Marks partial receipt of a purchase order.
     * Status transition: SENT → PARTIAL
     *
     * <p>Stock quantities are partially updated based on received items.</p>
     */
    @PatchMapping("/{id}/partial-receive")
    @Operation(summary = "Mark partial receipt of purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> partialReceiveOrder(@PathVariable Long id) {
        PurchaseOrderResponseDto partial = purchaseOrderService.partialReceiveOrder(id);
        return ResponseEntity.ok(ApiResponse.success(partial, "Purchase order partially received", HttpStatus.OK.value()));
    }

    /**
     * Marks complete receipt of a purchase order.
     * Status transition: SENT or PARTIAL → COMPLETED
     *
     * <p>Stock quantities are fully updated for all items.</p>
     */
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark purchase order as completed (fully received)")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> completeOrder(@PathVariable Long id) {
        PurchaseOrderResponseDto completed = purchaseOrderService.completeOrder(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Purchase order completed successfully", HttpStatus.OK.value()));
    }

    /**
     * Cancels a purchase order.
     * Can happen at any status except COMPLETED.
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> cancelOrder(@PathVariable Long id) {
        PurchaseOrderResponseDto cancelled = purchaseOrderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Purchase order cancelled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a purchase order by its ID, including items and supplier info.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderResponseDto>> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderResponseDto order = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Purchase order retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all purchase orders.
     */
    @GetMapping
    @Operation(summary = "Get all purchase orders")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDto>>> getAllPurchaseOrders() {
        List<PurchaseOrderResponseDto> orders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets purchase orders filtered by status.
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get purchase orders by status")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDto>>> getByStatus(@PathVariable PurchaseOrderStatus status) {
        List<PurchaseOrderResponseDto> orders = purchaseOrderService.getPurchaseOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets purchase orders from a specific supplier.
     */
    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get purchase orders by supplier")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDto>>> getBySupplier(@PathVariable Long supplierId) {
        List<PurchaseOrderResponseDto> orders = purchaseOrderService.getPurchaseOrdersBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets purchase orders within a date range.
     * Used for reports and auditing.
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get purchase orders by date range")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDto>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PurchaseOrderResponseDto> orders = purchaseOrderService.getPurchaseOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets overdue purchase orders.
     * Orders where expected delivery date has passed but not yet completed.
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue purchase orders")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponseDto>>> getOverdueOrders() {
        List<PurchaseOrderResponseDto> orders = purchaseOrderService.getOverduePurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Overdue purchase orders retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes a purchase order.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a purchase order")
    public ResponseEntity<ApiResponse<Void>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order deleted successfully", HttpStatus.OK.value()));
    }
}
