package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.dto.stock.StockCreateDto;
import hospital.inventoryservice.dto.stock.StockResponseDto;
import hospital.inventoryservice.dto.stock.StockUpdateDto;
import hospital.inventoryservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Stock management.
 * Provides CRUD operations and stock management APIs for drug batches.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Pharmacists: Full CRUD access</li>
 *   <li>Admin: Read access for inventory reports</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock Management", description = "Drug stock batch CRUD and management APIs")
public class StockApi {

    // ==================== Dependencies ====================

    private final StockService stockService;

    // ==================== Create ====================

    /**
     * Creates a new stock batch for a drug.
     *
     * <p><strong>Required fields:</strong> drugId, batchNumber, quantity</p>
     * <p><strong>Optional fields:</strong> minStockLevel, maxStockLevel, location, expiryDate</p>
     */
    @PostMapping
    @Operation(summary = "Create a new stock batch")
    public ResponseEntity<ApiResponse<StockResponseDto>> createStock(@Valid @RequestBody StockCreateDto createDto) {
        StockResponseDto created = stockService.createStock(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Stock batch created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing stock record.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a stock record by ID")
    public ResponseEntity<ApiResponse<StockResponseDto>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateDto updateDto) {
        StockResponseDto updated = stockService.updateStock(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Stock updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Increases stock quantity (e.g., when receiving a purchase order or returns).
     */
    @PatchMapping("/{id}/add")
    @Operation(summary = "Add quantity to stock")
    public ResponseEntity<ApiResponse<StockResponseDto>> addStock(
            @PathVariable Long id,
            @RequestParam int quantity) {
        StockResponseDto updated = stockService.addStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(updated, "Stock increased successfully", HttpStatus.OK.value()));
    }

    /**
     * Decreases stock quantity (e.g., when dispensing to a patient).
     */
    @PatchMapping("/{id}/remove")
    @Operation(summary = "Remove quantity from stock")
    public ResponseEntity<ApiResponse<StockResponseDto>> removeStock(
            @PathVariable Long id,
            @RequestParam int quantity) {
        StockResponseDto updated = stockService.removeStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(updated, "Stock decreased successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a stock record by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get stock by ID")
    public ResponseEntity<ApiResponse<StockResponseDto>> getStockById(@PathVariable Long id) {
        StockResponseDto stock = stockService.getStockById(id);
        return ResponseEntity.ok(ApiResponse.success(stock, "Stock retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all stock batches for a specific drug.
     */
    @GetMapping("/drug/{drugId}")
    @Operation(summary = "Get stocks by drug ID")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getStocksByDrug(@PathVariable Long drugId) {
        List<StockResponseDto> stocks = stockService.getStocksByDrug(drugId);
        return ResponseEntity.ok(ApiResponse.success(stocks, "Stocks retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all active stock records.
     */
    @GetMapping
    @Operation(summary = "Get all stocks")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getAllStocks() {
        List<StockResponseDto> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.success(stocks, "Stocks retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Alerts ====================

    /**
     * Gets expired stock batches.
     * Used for pharmacy quality control alerts.
     */
    @GetMapping("/alerts/expired")
    @Operation(summary = "Get expired stock batches")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getExpiredStocks() {
        List<StockResponseDto> stocks = stockService.getExpiredStocks();
        return ResponseEntity.ok(ApiResponse.success(stocks, "Expired stocks retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets stock batches expiring within a number of days.
     * Used for proactive expiry management.
     */
    @GetMapping("/alerts/expiring")
    @Operation(summary = "Get expiring stock batches")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getExpiringStocks(
            @RequestParam(defaultValue = "30") int days) {
        List<StockResponseDto> stocks = stockService.getExpiringStocks(days);
        return ResponseEntity.ok(ApiResponse.success(stocks, "Expiring stocks retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets low stock alerts (quantity below minStockLevel).
     * Used for reorder notifications.
     */
    @GetMapping("/alerts/low-stock")
    @Operation(summary = "Get low stock alerts")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getLowStockAlerts() {
        List<StockResponseDto> stocks = stockService.getLowStockAlerts();
        return ResponseEntity.ok(ApiResponse.success(stocks, "Low stock alerts retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Search ====================

    /**
     * Gets stocks by physical storage location.
     */
    @GetMapping("/location/{location}")
    @Operation(summary = "Get stocks by location")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getStocksByLocation(@PathVariable String location) {
        List<StockResponseDto> stocks = stockService.getStocksByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(stocks, "Stocks retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets total quantity of a drug across all batches.
     */
    @GetMapping("/drug/{drugId}/total-quantity")
    @Operation(summary = "Get total quantity of a drug")
    public ResponseEntity<ApiResponse<Integer>> getTotalQuantityByDrug(@PathVariable Long drugId) {
        int total = stockService.getTotalQuantityByDrug(drugId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total quantity retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes a stock record.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a stock record")
    public ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok(ApiResponse.success("Stock deleted successfully", HttpStatus.OK.value()));
    }
}
