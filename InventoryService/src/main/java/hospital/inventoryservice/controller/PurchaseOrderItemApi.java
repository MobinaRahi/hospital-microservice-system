package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemResponseDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.service.PurchaseOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for PurchaseOrderItem management.
 * Provides APIs for managing line items within purchase orders.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/purchase-order-items")
@RequiredArgsConstructor
@Tag(name = "Purchase Order Item Management", description = "Purchase order line item APIs")
public class PurchaseOrderItemApi {

    // ==================== Dependencies ====================

    private final PurchaseOrderItemService itemService;

    // ==================== Create ====================

    /**
     * Adds a new item to an existing purchase order.
     *
     * <p><strong>Required fields:</strong> drugId, quantity, unitPrice</p>
     * <p><strong>Optional fields:</strong> description</p>
     *
     * <p><strong>Rules:</strong> Only PENDING orders can have items added.</p>
     */
    @PostMapping("/order/{orderId}")
    @Operation(summary = "Add an item to a purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderItemResponseDto>> addItemToOrder(
            @PathVariable("orderId") Long purchaseOrderId,
            @Valid @RequestBody PurchaseOrderItemCreateDto createDto) {
        PurchaseOrderItemResponseDto created = itemService.addItemToOrder(purchaseOrderId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Item added to order successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing purchase order item.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a purchase order item")
    public ResponseEntity<ApiResponse<PurchaseOrderItemResponseDto>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderItemUpdateDto updateDto) {
        PurchaseOrderItemResponseDto updated = itemService.updateItem(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Item updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Records received quantity for an item.
     * <p>receivedQuantity cannot exceed ordered quantity.</p>
     * <p>Stock is automatically updated when items are received.</p>
     */
    @PatchMapping("/{id}/receive")
    @Operation(summary = "Record received quantity for an item")
    public ResponseEntity<ApiResponse<PurchaseOrderItemResponseDto>> recordReceived(
            @PathVariable Long id,
            @RequestParam int quantity) {
        PurchaseOrderItemResponseDto updated = itemService.recordReceived(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(updated, "Received quantity recorded successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a purchase order item by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order item by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderItemResponseDto>> getItemById(@PathVariable Long id) {
        PurchaseOrderItemResponseDto item = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item, "Item retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all items in a purchase order.
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all items in a purchase order")
    public ResponseEntity<ApiResponse<List<PurchaseOrderItemResponseDto>>> getItemsByOrder(@PathVariable Long orderId) {
        List<PurchaseOrderItemResponseDto> items = itemService.getItemsByOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(items, "Items retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all partially received items.
     * Items where receivedQuantity is less than ordered quantity.
     */
    @GetMapping("/partially-received")
    @Operation(summary = "Get partially received items")
    public ResponseEntity<ApiResponse<List<PurchaseOrderItemResponseDto>>> getPartiallyReceivedItems() {
        List<PurchaseOrderItemResponseDto> items = itemService.getPartiallyReceivedItems();
        return ResponseEntity.ok(ApiResponse.success(items, "Partially received items retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Removes an item from a purchase order.
     * <p>Only allowed for PENDING orders.</p>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove an item from a purchase order")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item removed successfully", HttpStatus.OK.value()));
    }
}
